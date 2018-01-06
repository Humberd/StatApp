package pl.swd.app.services.SpaceDivider

import org.springframework.stereotype.Service
import java.util.*

@Service
class SpaceDividerService {
    fun startAlgorithm(spaceDividerInput: SpaceDividerInput) {
        val (pointsList) = spaceDividerInput
        val axisesSize = determineAxisesSize(pointsList)
        val remainingSortedAxisesPoints = sortAxisesPointsAscending(pointsList, axisesSize)

        val allPotentialCutPoints = ArrayList<PointsToCutResposne>(axisesSize)
        for (index in 0..axisesSize) {
            val potentialCutPoints = findPointsToCut(remainingSortedAxisesPoints[index])
            allPotentialCutPoints.add(index, potentialCutPoints)
        }

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
}

data class SpaceDividerPoint(
        /**
         * Size of array indicates how many axises there are
         * For example: [1,2,3] means x=1, y=2, z=3
         */
        val axisesValues: Array<Float>,
        val decisionClass: String
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

class AxisesSizeMissmatchException(message: String) : Exception(message)

class EmptyListException(message: String) : Exception(message)

class InvalidIndexException(message: String) : Exception(message)