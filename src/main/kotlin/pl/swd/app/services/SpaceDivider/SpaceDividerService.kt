package pl.swd.app.services.SpaceDivider

import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class SpaceDividerService {
    fun startAlgorithm(spaceDividerInput: SpaceDividerInput) {
        val (pointsList) = spaceDividerInput
        val axisesSize = determineAxisesSize(pointsList)
        val initialSortedAxisesPoints = sortAxisesPointsAscending(pointsList, axisesSize)
        val remainingSortedAxisesPoints = sortAxisesPointsAscending(pointsList, axisesSize)

        /* 1. iteration */
        iteration(initialSortedAxisesPoints, remainingSortedAxisesPoints, axisesSize)

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
        val sortedAxises = ArrayList<List<SpaceDividerPoint>>(axisesSize)

        if (axisesSize == 0) return sortedAxises

        for (axisIndex in 0..axisesSize - 1) {
            val sortedPoints = pointsList.sortedWith(Comparator { o1, o2 -> o1.axisesValues[axisIndex].compareTo(o2.axisesValues[axisIndex]) })
            sortedAxises.add(axisIndex, sortedPoints)
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
     *
     */
    internal fun iteration(
            initialSortedAxisesPoints: List<List<SpaceDividerPoint>>,
            remainingSortedAxisesPoints: List<List<SpaceDividerPoint>>,
            axisesSize: Int
    ) {
        val allPotentialCutPoints = findAllPotentialCutPoints(remainingSortedAxisesPoints, axisesSize)
        val pointsToRemoveResponse = findMostPointsThatCanBeRemovedIn1Cut(allPotentialCutPoints)

        // adding vector values to all of the points
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
                if(point.axisesValues[pointsToRemoveResponse.axisIndex] <= pointsToRemoveResponse.cutLineValue) {
                    point.vector.add(0)
                } else {
                    point.vector.add(1)
                }
            }
        }

        // removing points from the remaining lists
        for (remainingSortedAxisPoints in remainingSortedAxisesPoints) {
            /* Remove the point [it] from remaining list when it is inside points to remove list */
            (remainingSortedAxisPoints as ArrayList).removeIf { pointsToRemoveResponse.pointsToRemoveIn1Cut.contains(it) }
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

data class SpaceDividerInput(
        val pointsList: List<SpaceDividerPoint>
)

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

class InvalidIndexException(message: String) : Exception(message)