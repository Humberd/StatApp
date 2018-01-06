package pl.swd.app.services.SpaceDivider

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = arrayOf("/test-beans.xml"))
class SpaceDividerServiceTest {
    @Autowired lateinit var spaceDividerService: SpaceDividerService

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class DetermineAxisesSize {
        @Test
        fun `should return axieses size equal to 0`() {
            val pointsList = listOf(
                    SpaceDividerPoint(arrayOf(), "foo"),
                    SpaceDividerPoint(arrayOf(), "foo"),
                    SpaceDividerPoint(arrayOf(), "foo"))

            val axisesSize = spaceDividerService.determineAxisesSize(pointsList)

            assertEquals(0, axisesSize)
        }

        @Test
        fun `should return axises size equal to 2`() {
            val pointsList = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 0f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 0f), "foo"))

            val axisesSize = spaceDividerService.determineAxisesSize(pointsList)

            assertEquals(2, axisesSize)
        }

        @Test
        fun `should return axises size equal to 5`() {
            val pointsList = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 0f, 1f, 23f, 4f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 0f, 1f, 23f, 4f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 0f, 1f, 23f, 4f), "foo"))

            val axisesSize = spaceDividerService.determineAxisesSize(pointsList)

            assertEquals(5, axisesSize)
        }

        @Test
        fun `should throw an exception when the points list is empty`() {
            val pointsList = emptyList<SpaceDividerPoint>()

            assertFailsWith<EmptyListException>("Points List cannot be empty") {
                spaceDividerService.determineAxisesSize(pointsList)
            }
        }

        @Test
        fun `should throw an exception when axises values sizes missmatch`() {
            val pointsList = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 0f, 2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 0f), "foo"))

            assertFailsWith<AxisesSizeMissmatchException> {
                spaceDividerService.determineAxisesSize(pointsList)
            }
        }
    }

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class SortAxisesPointsAscending {
        @Test
        fun `should sort 0 axises`() {
            val points = listOf(
                    SpaceDividerPoint(arrayOf(), "foo"),
                    SpaceDividerPoint(arrayOf(), "foo"),
                    SpaceDividerPoint(arrayOf(), "foo"))

            val sortedAxises = spaceDividerService.sortAxisesPointsAscending(points, 0)

            assertEquals(0, sortedAxises.size)
        }

        @Test
        fun `should sort 2 axieses`() {
            val points = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "foo"))

            val sortedAxises = spaceDividerService.sortAxisesPointsAscending(points, 2)

            assertEquals(2, sortedAxises.size)
            assert(sortedAxises[0] == arrayListOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "foo")))
            assert(sortedAxises[1] == arrayListOf(
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "foo"),
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "foo")))
        }

        @Test
        fun `should sort 5 axieses`() {
            val points = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f, 1.0f, 2.3f, 62f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f, 4.3f, 4.3f, 0.2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f, 0f, 0f, 0f), "foo"))

            val sortedAxises = spaceDividerService.sortAxisesPointsAscending(points, 5)

            assertEquals(5, sortedAxises.size)
            assert(sortedAxises[0] == arrayListOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f, 1.0f, 2.3f, 62f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f, 4.3f, 4.3f, 0.2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f, 0f, 0f, 0f), "foo")))
            assert(sortedAxises[1] == arrayListOf(
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f, 0f, 0f, 0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f, 1.0f, 2.3f, 62f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f, 4.3f, 4.3f, 0.2f), "foo")))
            assert(sortedAxises[2] == arrayListOf(
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f, 0f, 0f, 0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f, 1.0f, 2.3f, 62f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f, 4.3f, 4.3f, 0.2f), "foo")))
            assert(sortedAxises[3] == arrayListOf(
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f, 0f, 0f, 0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f, 1.0f, 2.3f, 62f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f, 4.3f, 4.3f, 0.2f), "foo")))
            assert(sortedAxises[4] == arrayListOf(
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f, 0f, 0f, 0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f, 4.3f, 4.3f, 0.2f), "foo"),
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f, 1.0f, 2.3f, 62f), "foo")))
        }
    }

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class FindPointsToCut {
        @Test
        fun `should find 3 negative and 3 positive points to cut`() {
            val points = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "foo"))

            val result = spaceDividerService.findPointsToCut(points)

            assert(result.negativeCutPoints == listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "foo")))

            assert(result.positiveCutPoints == listOf(
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "foo"),
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "foo")))
        }

        @Test
        fun `should find 1 negative and 1 positive points to cut`() {
            val points = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "ala"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "ma"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "kota"))

            val result = spaceDividerService.findPointsToCut(points)

            assert(result.negativeCutPoints == listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "ala")))

            assert(result.positiveCutPoints == listOf(
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "kota")))
        }

        @Test
        fun `should find 1 negative and 1 positive points to cut when the list size is 1`() {
            val points = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "ala"))

            val result = spaceDividerService.findPointsToCut(points)

            assert(result.negativeCutPoints == listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "ala")))

            assert(result.positiveCutPoints == listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "ala")))
        }

        @Test
        fun `should throw an exception when sorted axis points size is equal to 0`() {
            val points = emptyList<SpaceDividerPoint>()

            assertFailsWith<EmptyListException> {
                spaceDividerService.findPointsToCut(points)
            }
        }
    }
}