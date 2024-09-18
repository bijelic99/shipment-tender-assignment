package util

import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters._

object CsvParser {
  def parseFile[T](path: Path, sep: Char = ',', skipFirst: Boolean = true)(
      fn: Array[String] => T
  ): Seq[T] = {
    val lines = Files.readAllLines(path).asScala.toSeq
    (if (skipFirst) lines.drop(1) else lines)
      .map(_.split(sep).map(_.trim))
      .map(fn)
  }
}
