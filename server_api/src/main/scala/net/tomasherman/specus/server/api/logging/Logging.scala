package net.tomasherman.specus.server.api.logging

import org.slf4j.LoggerFactory
import ch.qos.logback.classic.{Level, Logger => logbackLogger}

/**
 * This file is part of Specus.
 *
 * Specus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Specus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with Specus.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

trait Logging {
  private val log = LoggerFactory.getLogger(getClass)

  def trace(message: String, values: Any*) {
    log.trace(message, values.map(_.asInstanceOf[Object]).toArray)
  }

  def trace(message: String, error: Throwable) {
    log.trace(message, error)
  }

  def trace(message: String, error: Throwable, values: Any*) {
    log.trace(message, error, values.map(_.asInstanceOf[Object]).toArray)
  }

  def debug(message: String, values: Any*) {
    log.debug(message, values.map(_.asInstanceOf[Object]).toArray)
  }

  def debug(message: String, error: Throwable) {
    log.debug(message, error)
  }

  def debug(message: String, error: Throwable, values: Any*) {
    log.debug(message, error, values.map(_.asInstanceOf[Object]).toArray)
  }

  def info(message: String, values: Any*) {
    log.info(message, values.map(_.asInstanceOf[Object]).toArray)
  }

  def info(message: String, error: Throwable) {
    log.info(message, error)
  }

  def info(message: String, error: Throwable, values: Any*) {
    log.info(message, error, values.map(_.asInstanceOf[Object]).toArray)
  }

  def warn(message: String, values: Any*) {
    log.warn(message, values.map(_.asInstanceOf[Object]).toArray)
  }

  def warn(message: String, error: Throwable) {
    log.warn(message, error)
  }

  def warn(message: String, error: Throwable, values: Any*) {
    log.warn(message, error, values.map(_.asInstanceOf[Object]).toArray)
  }

  def error(message: String, values: Any*) {
    log.error(message, values.map(_.asInstanceOf[Object]).toArray)
  }

  def error(message: String, error: Throwable) {
    log.error(message, error)
  }

  def error(message: String, error: Throwable, values: Any*) {
    log.error(message, error, values.map(_.asInstanceOf[Object]).toArray)
  }

  def disableLogging() {
    setLoggingLevel(Level.OFF)
  }

  def enableLoggingDebug() {
    setLoggingLevel(Level.DEBUG)
  }

  def enableLoggingWarn() {
    setLoggingLevel(Level.WARN)
  }

  def enableLoggingInfo() {
    setLoggingLevel(Level.INFO)
  }

  def enableLoggingTrace() {
    setLoggingLevel(Level.TRACE)
  }

  def enableLoggingError() {
    setLoggingLevel(Level.ERROR)
  }

  private def setLoggingLevel(l: Level) {
    log.asInstanceOf[logbackLogger].setLevel(l)
  }

  protected def loggingLevel = log.asInstanceOf[logbackLogger].getLevel

  def offTheRecords[A](f: => A): A = {
    val lvl = loggingLevel
    disableLogging()
    val result = f
    setLoggingLevel(lvl)
    result
  }

}
