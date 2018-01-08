package pl.swd.app.services.SpaceDivider

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import kotlin.collections.ArrayList
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

        @Test
        fun `should return a list of linked lists`() {
            val points = listOf(
                    SpaceDividerPoint(arrayOf(1.0f, 4.0f), "foo"),
                    SpaceDividerPoint(arrayOf(1.5f, 5.2f), "foo"),
                    SpaceDividerPoint(arrayOf(2.0f, 1.2f), "foo"))

            val sortedAxises = spaceDividerService.sortAxisesPointsAscending(points, 2)

            assertEquals(2, sortedAxises.size)
            for (sortedAxis in sortedAxises) {
                assert(sortedAxis is LinkedList)
            }
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

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class FindAllPotentialCutPoints {
        @Test
        fun `should find all potential cut points`() {
            val remainingSortedAxisesPoints = listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1.0f, 0.5f), "a"),
                            SpaceDividerPoint(arrayOf(1.5f, 3f), "b"),
                            SpaceDividerPoint(arrayOf(2.0f, 2.0f), "c")),
                    listOf(
                            SpaceDividerPoint(arrayOf(1.0f, 0.5f), "a"),
                            SpaceDividerPoint(arrayOf(2.0f, 2.0f), "c"),
                            SpaceDividerPoint(arrayOf(1.5f, 3f), "b"))
            )

            val result = spaceDividerService.findAllPotentialCutPoints(remainingSortedAxisesPoints, 2)

            assert(result == listOf(
                    PointsToCutResposne(
                            negativeCutPoints = listOf(SpaceDividerPoint(arrayOf(1.0f, 0.5f), "a")),
                            positiveCutPoints = listOf(SpaceDividerPoint(arrayOf(2.0f, 2.0f), "c"))
                    ),
                    PointsToCutResposne(
                            negativeCutPoints = listOf(SpaceDividerPoint(arrayOf(1.0f, 0.5f), "a")),
                            positiveCutPoints = listOf(SpaceDividerPoint(arrayOf(1.5f, 3f), "b"))
                    )
            ))
        }
    }

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class FindMostPointsThatCanBeRemovedIn1Cut {
        @Test
        fun `should find list with 2 points when there is only 1 list with 2 points`() {
            val allPotentialCutPoints = listOf(
                    PointsToCutResposne(
                            positiveCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(1f, 1f), "a"),
                                    SpaceDividerPoint(arrayOf(2f, 2f), "b")),
                            negativeCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(0f, 0f), "c"))
                    ),
                    PointsToCutResposne(
                            positiveCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(3f, 3f), "d")),
                            negativeCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(4f, 4f), "e"))
                    )
            )

            val response = spaceDividerService.findMostPointsThatCanBeRemovedIn1Cut(allPotentialCutPoints)

            assert(response == PointsToRemoveIn1CutResponse(
                    axisIndex = 0,
                    isPositive = true,
                    cutLineValue = 1f,
                    pointsToRemoveIn1Cut = listOf(
                            SpaceDividerPoint(arrayOf(1f, 1f), "a"),
                            SpaceDividerPoint(arrayOf(2f, 2f), "b"))
            ))
        }

        @Test
        fun `should find the first list with 2 points when there are 3 lists with 2 points`() {
            val allPotentialCutPoints = listOf(
                    PointsToCutResposne(
                            positiveCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(1f, 1f), "a"),
                                    SpaceDividerPoint(arrayOf(2f, 2f), "b")),
                            negativeCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(0f, 0f), "c"))
                    ),
                    PointsToCutResposne(
                            positiveCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(4f, 4f), "d"),
                                    SpaceDividerPoint(arrayOf(5f, 5f), "e")),
                            negativeCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(6f, 6f), "e"),
                                    SpaceDividerPoint(arrayOf(6f, 6f), "f"))
                    )
            )

            val response = spaceDividerService.findMostPointsThatCanBeRemovedIn1Cut(allPotentialCutPoints)

            assert(response == PointsToRemoveIn1CutResponse(
                    axisIndex = 0,
                    isPositive = true,
                    cutLineValue = 1f,
                    pointsToRemoveIn1Cut = listOf(
                            SpaceDividerPoint(arrayOf(1f, 1f), "a"),
                            SpaceDividerPoint(arrayOf(2f, 2f), "b"))
            ))
        }

        @Test
        fun `should find the first list with 5 points when there are 2 list with 5 points`() {
            val allPotentialCutPoints = listOf(
                    PointsToCutResposne(
                            positiveCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(1f, 1f), "a"),
                                    SpaceDividerPoint(arrayOf(2f, 2f), "b")),
                            negativeCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(3f, 3f), "c"),
                                    SpaceDividerPoint(arrayOf(4f, 4f), "d"),
                                    SpaceDividerPoint(arrayOf(5f, 5f), "e"),
                                    SpaceDividerPoint(arrayOf(6f, 6f), "f"),
                                    SpaceDividerPoint(arrayOf(7f, 7f), "g"))
                    ),
                    PointsToCutResposne(
                            positiveCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(8f, 8f), "h"),
                                    SpaceDividerPoint(arrayOf(9f, 9f), "i"),
                                    SpaceDividerPoint(arrayOf(10f, 10f), "j"),
                                    SpaceDividerPoint(arrayOf(11f, 11f), "k"),
                                    SpaceDividerPoint(arrayOf(12f, 12f), "l")),
                            negativeCutPoints = listOf(
                                    SpaceDividerPoint(arrayOf(13f, 13f), "m"),
                                    SpaceDividerPoint(arrayOf(14f, 14f), "n"))
                    )
            )

            val response = spaceDividerService.findMostPointsThatCanBeRemovedIn1Cut(allPotentialCutPoints)

            assert(response == PointsToRemoveIn1CutResponse(
                    axisIndex = 0,
                    isPositive = false,
                    cutLineValue = 7f,
                    pointsToRemoveIn1Cut = listOf(
                            SpaceDividerPoint(arrayOf(3f, 3f), "c"),
                            SpaceDividerPoint(arrayOf(4f, 4f), "d"),
                            SpaceDividerPoint(arrayOf(5f, 5f), "e"),
                            SpaceDividerPoint(arrayOf(6f, 6f), "f"),
                            SpaceDividerPoint(arrayOf(7f, 7f), "g"))
            ))
        }
    }

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class AddVectorValuesToAllPoints {
        @Test
        fun `should add the first vector points to the 2 first negative points from axis x`() {
            val initialSortedAxisesPoints = listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf()),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf()),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf()),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf()),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf()),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf())
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf()),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf()),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf()),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf()),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf()),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf())
                    )
            )
            val pointsToRemoveIn1Cut = PointsToRemoveIn1CutResponse(
                    axisIndex = 0,
                    isPositive = false,
                    cutLineValue = 2f,
                    pointsToRemoveIn1Cut = listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a"),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b")
                    )
            )

            spaceDividerService.addVectorValuesToAllPoints(initialSortedAxisesPoints, pointsToRemoveIn1Cut)

            assert(initialSortedAxisesPoints == listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(1))
                    )
            ))
        }

        @Test
        fun `should add the second vector points to the 1 first positive points from axis y`() {
            val initialSortedAxisesPoints = listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0))
                    )
            )
            val pointsToRemoveIn1Cut = PointsToRemoveIn1CutResponse(
                    axisIndex = 1,
                    isPositive = true,
                    cutLineValue = 7.4f,
                    pointsToRemoveIn1Cut = listOf(
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0))
                    )
            )

            spaceDividerService.addVectorValuesToAllPoints(initialSortedAxisesPoints, pointsToRemoveIn1Cut)

            assert(initialSortedAxisesPoints == listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0, 1)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1, 0))
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1, 0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0, 1))
                    )
            ))
        }

        @Test
        fun `should add the second vector points to the 2 middle positive points from axis y`() {
            val initialSortedAxisesPoints = listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0))
                    )
            )
            val pointsToRemoveIn1Cut = PointsToRemoveIn1CutResponse(
                    axisIndex = 1,
                    isPositive = true,
                    cutLineValue = 1.5f,
                    pointsToRemoveIn1Cut = listOf(
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    )
            )

            spaceDividerService.addVectorValuesToAllPoints(initialSortedAxisesPoints, pointsToRemoveIn1Cut)

            assert(initialSortedAxisesPoints == listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0, 1)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0, 1)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0, 1)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1, 1))
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0, 0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0, 1)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1, 1)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0, 1)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0, 1))
                    )
            ))
        }
    }

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class RemovePointsFromRemainingLists {
        @Test
        fun `should remove 2 points from both arrays based on referential equality`() {
            /* Need to use the same objects, because removing points will be based on referential equality */
            val point1ToRemove = SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0))
            val point2ToRemove = SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0))
            val remainingSortedAxisesPoints = listOf(
                    LinkedList(listOf(
                            point1ToRemove,
                            point2ToRemove,
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    )),
                    LinkedList(listOf(
                            point1ToRemove,
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1)),
                            point2ToRemove,
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0))
                    ))
            )
            val pointsToRemoveIn1Cut = PointsToRemoveIn1CutResponse(
                    axisIndex = 0,
                    isPositive = false,
                    cutLineValue = 2f,
                    pointsToRemoveIn1Cut = listOf(point1ToRemove, point2ToRemove)
            )

            spaceDividerService.removePointsFromRemainingLists(remainingSortedAxisesPoints, pointsToRemoveIn1Cut)

            assert(remainingSortedAxisesPoints == listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0))
                    )
            ))
        }

        @Test
        fun `should not remove 2 points from both arrays when the references does not match`() {
            val remainingSortedAxisesPoints = listOf(
                    LinkedList(listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    )),
                            LinkedList(listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0))
                    ))
            )
            val pointsToRemoveIn1Cut = PointsToRemoveIn1CutResponse(
                    axisIndex = 0,
                    isPositive = false,
                    cutLineValue = 2f,
                    pointsToRemoveIn1Cut = listOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0))
                    )
            )

            spaceDividerService.removePointsFromRemainingLists(remainingSortedAxisesPoints, pointsToRemoveIn1Cut)

            assert(remainingSortedAxisesPoints == listOf(
                    arrayListOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1))
                    ),
                    arrayListOf(
                            SpaceDividerPoint(arrayOf(1f, 0.5f), "a", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5.2f, 1.2f), "e", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "c", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(6f, 1.5f), "f", arrayListOf(1)),
                            SpaceDividerPoint(arrayOf(2f, 4.2f), "b", arrayListOf(0)),
                            SpaceDividerPoint(arrayOf(5f, 7.4f), "d", arrayListOf(0))
                    )
            ))
        }
    }

    @Nested
    @ContextConfiguration(locations = arrayOf("/test-beans.xml"))
    inner class InitializeAlgorithm {
        @Test
        fun `4 points, 2 cuts, Case 1`() {
            val pointsList = listOf(
                    SpaceDividerPoint(arrayOf(1f, 1f), "c"),
                    SpaceDividerPoint(arrayOf(3f, 1.5f), "b"),
                    SpaceDividerPoint(arrayOf(1.5f, 3f), "a"),
                    SpaceDividerPoint(arrayOf(4f, 2f), "a")
            )
            val initialSortedPoints = listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 1f), "c"),
                            SpaceDividerPoint(arrayOf(1.5f, 3f), "a"),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "b"),
                            SpaceDividerPoint(arrayOf(4f, 2f), "a")
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 1f), "c"),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "b"),
                            SpaceDividerPoint(arrayOf(4f, 2f), "a"),
                            SpaceDividerPoint(arrayOf(1.5f, 3f), "a")
                    )
            )

            val worker = spaceDividerService.initializeAlgorithm(pointsList)

            assertEquals(2, worker.axisesSize)
            /* Make sure 2 lists have the same content before 1 iteration */
            assert(worker.initialSortedAxisesPoints == worker.remainingSortedAxisesPoints)
            assert(worker.remainingSortedAxisesPoints == initialSortedPoints)

            /* 1st iteration */
            worker.nextIteration()

            assert(worker.initialSortedAxisesPoints == initialSortedPoints)
            assert(worker.remainingSortedAxisesPoints == listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 1f), "c"),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "b")
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(1f, 1f), "c"),
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "b")
                    )
            ))

            /* 2nd iteration */
            worker.nextIteration()
            assert(worker.initialSortedAxisesPoints == initialSortedPoints)
            assert(worker.remainingSortedAxisesPoints == listOf(
                    listOf(
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "b")
                    ),
                    listOf(
                            SpaceDividerPoint(arrayOf(3f, 1.5f), "b")
                    )
            ))

            /* 3rd iteration */
            assertFailsWith<IterationsAlreadyCompletedException> {
                worker.nextIteration()
            }
        }

        @Test
        fun `100 points, 2 axises, 2 decision classes load test`() {
            val pointsList = generateRandomPointsList(
                    numberOfPoints = 100,
                    pointValuesFrom = 0.0,
                    pointValuesTo = 50.0,
                    numberOfAxises = 2,
                    numberOfDecisionClasses = 2
            )

            val worker = spaceDividerService.initializeAlgorithm(pointsList)

            worker.completeAllIterations()
        }

        @Test
        fun `10_000 points, 2 axises, 2 decision classes load test`() {
            val pointsList = generateRandomPointsList(
                    numberOfPoints = 10_000,
                    pointValuesFrom = 0.0,
                    pointValuesTo = 50.0,
                    numberOfAxises = 2,
                    numberOfDecisionClasses = 2
            )

            val worker = spaceDividerService.initializeAlgorithm(pointsList)

            worker.completeAllIterations()
        }

        @Test
        fun `10_000 points, 4 axises, 2 decision classes load test`() {
            val pointsList = generateRandomPointsList(
                    numberOfPoints = 10_000,
                    pointValuesFrom = 0.0,
                    pointValuesTo = 50.0,
                    numberOfAxises = 2,
                    numberOfDecisionClasses = 2
            )

            val worker = spaceDividerService.initializeAlgorithm(pointsList)

            worker.completeAllIterations()
        }

        @Test
        fun `10_000 points, 4 axises, 5 decision classes load test`() {
            val pointsList = generateRandomPointsList(
                    numberOfPoints = 10_000,
                    pointValuesFrom = 0.0,
                    pointValuesTo = 50.0,
                    numberOfAxises = 4,
                    numberOfDecisionClasses = 5
            )

            val worker = spaceDividerService.initializeAlgorithm(pointsList)

            worker.completeAllIterations()
        }

        /* @Test
         fun `1_000_000 points, 2 axises, 2 decision classes load test`() {
             val pointsList = generateRandomPointsList(
                     numberOfPoints = 1_000_000,
                     pointValuesFrom = 0.0,
                     pointValuesTo = 50.0,
                     numberOfAxises = 2,
                     numberOfDecisionClasses = 2
             )

             val worker = spaceDividerService.initializeAlgorithm(pointsList)

             worker.completeAllIterations()
         }*/

    }
}

fun generateRandomPointsList(
        numberOfPoints: Int,
        pointValuesFrom: Double,
        pointValuesTo: Double,
        numberOfAxises: Int,
        numberOfDecisionClasses: Int
): ArrayList<SpaceDividerPoint> {
    val axisesArrays = ArrayList<DoubleArray>(numberOfAxises)
    for (i in 0..numberOfAxises - 1) {
        axisesArrays.add(i, Random().doubles(numberOfPoints.toLong(), pointValuesFrom, pointValuesTo).toArray())
    }

    val decisionClassesPool = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")

    val pointsList = ArrayList<SpaceDividerPoint>(numberOfPoints)

    for (i in 0..numberOfPoints - 1) {
        val axisesValues = ArrayList<Float>(numberOfAxises)
        for (j in 0..numberOfAxises - 1) {
            axisesValues.add(j, axisesArrays[j][i].toFloat())
        }

        pointsList.add(i, SpaceDividerPoint(axisesValues.toTypedArray(), decisionClassesPool[Random().nextInt(numberOfDecisionClasses)]))
    }

    return pointsList
}