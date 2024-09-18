package repository

import com.google.inject.ImplementedBy
import model.{Country, Provider, Rate, Shipment}
import repository.inMemory.InMemoryRateRepository

import scala.collection.immutable.SortedSet

@ImplementedBy(classOf[InMemoryRateRepository])
trait RateRepository {
  def getApplicableRatesPerProvider(
      countryWeightMap: Map[Country, Set[Double]]
  ): Map[Provider, Map[Country, SortedSet[Rate]]]
}
