package pl.swd.app.services.SpaceDivider

import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class SpaceDividerService {
    companion object: KLogging()

    fun initializeAlgorithm(pointsList: List<SpaceDividerPoint>): SpaceDividerWorker {
        val axisesSize = determineAxisesSize(pointsList)
        val initialSortedAxisesPoints = sortAxisesPointsAscending(pointsList, axisesSize)
        val remainingSortedAxisesPoints = sortAxisesPointsAscending(pointsList, axisesSize)

        return SpaceDividerWorker(initialSortedAxisesPoints, remainingSortedAxisesPoints, axisesSize)
    }

    inner class SpaceDividerWorker(
            val initialSortedAxisesPoints: List<List<SpaceDividerPoint>>,
            val remainingSortedAxisesPoints: List<List<SpaceDividerPoint>>,
            val axisesSize: Int
    ) {
        val iterationsResults = ArrayList<PointsToRemoveIn1CutResponse>()

        fun nextIteration(): PointsToRemoveIn1CutResponse {
            validateOperationsCompleteness()

            val allPotentialCutPoints = findAllPotentialCutPoints(remainingSortedAxisesPoints, axisesSize)
            val pointsToRemoveResponse = findMostPointsThatCanBeRemovedIn1Cut(allPotentialCutPoints)

            addVectorValuesToAllPoints(initialSortedAxisesPoints, pointsToRemoveResponse)

            removePointsFromRemainingLists(remainingSortedAxisesPoints, pointsToRemoveResponse)

            iterationsResults.add(pointsToRemoveResponse)
            return pointsToRemoveResponse
        }

        fun completeAllIterations() {
            var i = 0
            try {
                while (true) {
                    nextIteration()
                    logger.debug { "Iteration ${++i} completed. Remaining size ${remainingSortedAxisesPoints.first().size}" }
                }
            } catch (e: IterationsAlreadyCompletedException) {

            }
        }

        internal fun validateOperationsCompleteness() {
            if (axisesSize <= 0) {
                throw IterationsAlreadyCompletedException("Cannot iterate, because axises size is $axisesSize")
            }

            val potentialOnlyDecisionClass = remainingSortedAxisesPoints.first().first().decisionClass

            /* When every remaining point has the same decision class */
            if (remainingSortedAxisesPoints.all { remainingSortedAxisPoints ->
                remainingSortedAxisPoints.all { it.decisionClass.equals(potentialOnlyDecisionClass) }
            }) {
                throw IterationsAlreadyCompletedException("Every remaining point has the same decision class")
            }
        }
    }

    /**
     * Validates if every point has the same declared number of axises.
     * Returns number of axieses
     */
    internal fun determineAxisesSize(pointsList: List<SpaceDividerPoint>): Int {
        if (pointsList.isEmpty()) throw EmptyListException("Points List cannot be empty")

        val probableAxiesesSize = pointsList.first().axisesValues.size
        val allHasTheSameSize = pointsList.all { it.axisesValues.size == probableAxiesesSize }

        if (!allHasTheSameSize) throw AxisesSizeMissmatchException("Points does not have the same axises values size")

        return probableAxiesesSize
    }

    /**
     * Returns a listof axises.
     * Each element of a list contains a list of points that are sorted by corresponding axis index
     */
    internal fun sortAxisesPointsAscending(pointsList: List<SpaceDividerPoint>, axisesSize: Int): List<List<SpaceDividerPoint>> {
        val sortedAxises = ArrayList<LinkedList<SpaceDividerPoint>>(axisesSize)

        if (axisesSize == 0) return sortedAxises

        for (axisIndex in 0..axisesSize - 1) {
            val sortedPoints = pointsList.sortedWith(Comparator { o1, o2 -> o1.axisesValues[axisIndex].compareTo(o2.axisesValues[axisIndex]) })
            sortedAxises.add(axisIndex, LinkedList(sortedPoints))
        }

        return sortedAxises
    }

    /**
     * Finds left and right edge points with the same decision class
     * For example: ["a","b","c","c"]
     * It returns: {
     *  negativeCutPoints: ["a"]
     *  positiveCutPoints: ["c","c"]
     * }
     */
    internal fun findPointsToCut(sortedAxisPoints: List<SpaceDividerPoint>): PointsToCutResposne {
        if (sortedAxisPoints.isEmpty()) throw EmptyListException("List cannot be empty")

        val negativePointDecisionClass = sortedAxisPoints.first().decisionClass
        val negativeCutPoints = arrayListOf<SpaceDividerPoint>()
        for (i in 0..sortedAxisPoints.size - 1) {
            if (sortedAxisPoints[i].decisionClass !== negativePointDecisionClass) {
                break
            }
            negativeCutPoints.add(sortedAxisPoints[i])
        }

        val positivePointDecisionClass = sortedAxisPoints.last().decisionClass
        val positiveCutPoints = arrayListOf<SpaceDividerPoint>()
        for (i in sortedAxisPoints.size - 1 downTo 0) {
            if (sortedAxisPoints[i].decisionClass !== positivePointDecisionClass) {
                break
            }
            positiveCutPoints.add(sortedAxisPoints[i])
        }

        return PointsToCutResposne(
                negativeCutPoints = negativeCutPoints,
                positiveCutPoints = positiveCutPoints
        )
    }

    /**
     *  Generates a list of all potential points to cut among [remainingSortedAxisesPoints]
     *  When there are 2 axies it generates:
     *   * 2 lists for axis X (left and right) or (positive and negative)
     *   * 2 lists for axis Y (bottom and top) or (positive and negative)
     */
    internal fun findAllPotentialCutPoints(remainingSortedAxisesPoints: List<List<SpaceDividerPoint>>, axisesSize: Int): List<PointsToCutResposne> {
        val allPotentialCutPoints = ArrayList<PointsToCutResposne>(axisesSize)
        for (index in 0..axisesSize - 1) {
            val potentialCutPoints = findPointsToCut(remainingSortedAxisesPoints[index])
            allPotentialCutPoints.add(index, potentialCutPoints)
        }
        return allPotentialCutPoints
    }

    /**
     * Returns [SpaceDividerPoint] list which has the biggest size from within [allPotentialCutPoints]
     * When there are several lists with the same size it takes the first one.
     */
    internal fun findMostPointsThatCanBeRemovedIn1Cut(allPotentialCutPoints: List<PointsToCutResposne>): PointsToRemoveIn1CutResponse {
        if (allPotentialCutPoints.isEmpty()) throw EmptyListException("All potential cut points cannot be empty")

        val response = PointsToRemoveIn1CutResponse(
                axisIndex = 0,
                isPositive = false,
                cutLineValue = allPotentialCutPoints.first().negativeCutPoints.last().axisesValues[0],
                pointsToRemoveIn1Cut = allPotentialCutPoints.first().negativeCutPoints
        )
        allPotentialCutPoints.forEachIndexed { index, potentialAxisCutPoints ->
            if (potentialAxisCutPoints.negativeCutPoints.size > response.pointsToRemoveIn1Cut.size) {
                with(response) {
                    axisIndex = index
                    isPositive = false
                    cutLineValue = potentialAxisCutPoints.negativeCutPoints.last().axisesValues[index]
                    pointsToRemoveIn1Cut = potentialAxisCutPoints.negativeCutPoints
                }
            }
            if (potentialAxisCutPoints.positiveCutPoints.size > response.pointsToRemoveIn1Cut.size) {
                with(response) {
                    axisIndex = index
                    isPositive = true
                    cutLineValue = potentialAxisCutPoints.positiveCutPoints.first().axisesValues[index]
                    pointsToRemoveIn1Cut = potentialAxisCutPoints.positiveCutPoints
                }
            }
        }

        return response
    }

    /**
     * When providing axis with values [1,2,3,4,5,6,7] and a [cutlineValue] of 5 with [positive] cut
     * It will add vector values like this [1(0), 2(0), 3(0), 4(0), 5(1), 6(1), 7(1)]
     *
     * When providing axis with values [1,2,3,4,5,6,7] and a [cutlineValue] of 5 with [negative] cut
     * It will add vector values like this [1(0), 2(0), 3(0), 4(0), 5(0), 6(1), 7(1)]
     *
     * Take a look at value 5(*). When the cut is positive the [1] value in a vector in inclusive
     */
    internal fun addVectorValuesToAllPoints(
            initialSortedAxisesPoints: List<List<SpaceDividerPoint>>,
            pointsToRemoveResponse: PointsToRemoveIn1CutResponse
    ) {
        for (point in initialSortedAxisesPoints[pointsToRemoveResponse.axisIndex]) {
            if (pointsToRemoveResponse.isPositive) {
                /* When the value on the axis is on the left to the cut line then we should add 0 to the vector */
                if (point.axisesValues[pointsToRemoveResponse.axisIndex] < pointsToRemoveResponse.cutLineValue) {
                    point.vector.add(0)
                } else {
                    point.vector.add(1)
                }
            } else {
                /* When the value on the axis is on the left to the cut line OR ON THE CUTLINE then we should add 0 to the vector */
                if (point.axisesValues[pointsToRemoveResponse.axisIndex] <= pointsToRemoveResponse.cutLineValue) {
                    point.vector.add(0)
                } else {
                    point.vector.add(1)
                }
            }
        }
    }

    /**
     * Removes points from all [remainingSortedAxisesPoints] based on refferential equality
     */
    internal fun removePointsFromRemainingLists(
            remainingSortedAxisesPoints: List<List<SpaceDividerPoint>>,
            pointsToRemoveResponse: PointsToRemoveIn1CutResponse
    ) {
        for (remainingSortedAxisPoints in remainingSortedAxisesPoints) {
            /* Remove the point [it] from remaining list when it is inside points to remove list */
            (remainingSortedAxisPoints as LinkedList).removeIf { potentialPointToRemove ->
                pointsToRemoveResponse.pointsToRemoveIn1Cut.any { it === potentialPointToRemove }
            }
        }
    }
}

data class SpaceDividerPoint(
        /**
         * Size of array indicates how many axises there are
         * For example: [1,2,3] means x=1, y=2, z=3
         */
        val axisesValues: Array<Float>,
        val decisionClass: String,
        val vector: ArrayList<Int> = ArrayList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpaceDividerPoint

        if (!Arrays.equals(axisesValues, other.axisesValues)) return false
        if (decisionClass != other.decisionClass) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(axisesValues)
        result = 31 * result + decisionClass.hashCode()
        return result
    }
}

data class PointsToCutResposne(
        var positiveCutPoints: List<SpaceDividerPoint>,
        var negativeCutPoints: List<SpaceDividerPoint>
)

data class PointsToRemoveIn1CutResponse(
        /**
         * When we remove points from axis x, this value will be 0
         */
        var axisIndex: Int,
        /**
         * When we remove points from the right side of the axis it will be true
         * When we remove points form the left side of the axis it will be false
         */
        var isPositive: Boolean,
        /**
         * Value on the axis [axisIndex] where the line should be placed to make a cut
         * It's the value from the nearest [pointsToRemoveIn1Cut] to the rest of the points
         */
        var cutLineValue: Float,
        /**
         * List of points to remove from remaining points list
         */
        var pointsToRemoveIn1Cut: List<SpaceDividerPoint>
)

class AxisesSizeMissmatchException(message: String) : Exception(message)

class EmptyListException(message: String) : Exception(message)

class IterationsAlreadyCompletedException(message: String) : Exception(message)