package dal

import play.db.anorm._
import play.db.anorm.SqlParser._
import play.db.anorm.defaults._
import org.joda.time.{Duration, LocalTime, DateTime}
import java.text.SimpleDateFormat
import org.joda.time.format.DateTimeFormat
import play.Logger
import java.util.{Calendar, UUID, Date}

case class User(id: Pk[Long], token: String, email: String, name: Option[String], billingInfo: Option[String]) {

  def isActive = name.isDefined

}

case class Event(id: Pk[Long], name: String, owner_id: Long, token: String, isDone: Boolean)

case class Payment(id: Pk[Long], event_id: Long, payer_id: Long, forWho: String, amount: Double,
                   creationDate: Date, description: String)

object Event extends Magic[Event] {

  def create(name: String, owner_id: Long, participantIds: Seq[Long]): Event = {
    val token = UUID.randomUUID().toString
    insert(Event(NotAssigned, name, owner_id, token, false)).fold(
      e => throw new Exception(e.message),
      x => ()
    )

    val event = byToken(token)

    SQL(
      "INSERT INTO EventUser (event_id, user_id) VALUES " +
      (Seq(owner_id) ++ participantIds).map("(" + event.id() + "," + _ + ")").mkString(", ")
    ).executeUpdate().fold(
      e => throw new Exception(e.message),
      x => ()
    )

    event
  }

  def byToken(token: String): Event =
    find("token = {t}").on("t" -> token).single()


}

object User extends Magic[User] {

  def create(email: String, name: Option[String] = None): User = {
    val token = UUID.randomUUID().toString
    insert(User(NotAssigned, token, email, name, None)).fold(
      e => throw new Exception(e.message),
      x => ()
    )
    find("token = {t}").on("t" -> token).single()
  }

  def allForEvent(event_id: Long): Seq[User] = {
    SQL("""
    SELECT u.* FROM User u
    INNER JOIN EventUser eu on u.id = eu.user_id
    WHERE eu.event_id = {eid}
    """).on(
      "eid" -> event_id
    ).as(User *)
  }


}

object Payment extends Magic[Payment] {

  def allForEvent(event_id: Long): Seq[Payment] =
    find("event_id = {eid}").on("eid" -> event_id).list()

  def create(event_id: Long, description: String, forWho: Seq[Long], payer_id: Long, amount: Double) {

    insert(Payment(
      NotAssigned, event_id, payer_id, forWho.mkString(","), amount, Calendar.getInstance().getTime, description
    )).fold(
      e => throw new Exception(e.message),
      x => ()
    )
  }


}