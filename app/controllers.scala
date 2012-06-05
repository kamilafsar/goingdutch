package controllers

import algorithms.Algorithms
import dal.{Payment, Event, User}
import play._
import libs.{Mail, Time}
import play.mvc._
import scala.collection.JavaConversions._
import org.apache.commons.mail.SimpleEmail

object Application extends Controller {

  import views.Application._

  private def setCookies(userToken: String, eventToken: String) {
    response.setCookie("USER_KEY", userToken, null, "/", Time.parseDuration("365d"), false, false)
    response.setCookie("EVENT_KEY", eventToken, null, "/", Time.parseDuration("365d"), false, false)
  }

  def index = {
    html.index("Your Scala application is ready!")
  }

  def start(ownerName: String, ownerEmail: String, eventName: String, eventEmails: String, message: String) = {
    val owner = User.create(ownerEmail, Some(ownerName))
    val users = eventEmails.split(",").map(_.trim).map(email => User.create(email))
    val event = Event.create(eventName, owner.id(), users.map(_.id()))

    users.foreach { user =>
      val email = new SimpleEmail()
      email.setFrom(ownerEmail)
      email.addTo(user.email)
      email.setSubject("Going Dutch Invite")
      email.setMsg("Message + " + "http://" + play.Play.configuration.get("baseUrl")  + "/invitation?token=" + user.token)
      Mail.send(email)
    }

    setCookies(owner.token, event.token)

    Redirect("/board/" + play.libs.WS.encode(eventName) + "/" + event.token)
  }

  def board(token: String) = {
    val event = Event.byToken(token)
    val participants = User.allForEvent(event.id())
    val payments = Payment.allForEvent(event.id())

    html.board(event, Algorithms.makeUpBalance(participants, payments), payments)
  }


  def createPayment(description: String, participant: Array[Long]) = {
    val eventCookie = request.cookies.get("EVENT_KEY")

    if (eventCookie == null) {
      Error("Event cookie should be set")
    }
    else {
      val event = Event.byToken(eventCookie.value)

      val payerAmounts = request.params.allSimple().flatMap {
        case (key: String, payerId: String) if key.startsWith("payer_") =>
          val index = key.substring("payer_".length).toInt
          val amount = request.params.get("amount_" + index)
          Some((payerId.toInt, amount.toDouble))
        case _ =>
          None
      }

      payerAmounts.map {
        case (payerId, amount) =>
          Payment.create(event.id(), description, participant.toSeq, payerId, amount)
      }

      Redirect("/board/" + play.libs.WS.encode(event.name) + "/" + event.token)
    }
  }

  def invitation(token: String) = {
    html.invitation(Event.byUserToken(token), token)
  }

  def invitationAccepted(token: String, name: String) = {
    val event = Event.byUserToken(token)
    User.update(User.byToken(token).copy(name = Some(name)))
    setCookies(token, event.token)

    Redirect("/board/" + play.libs.WS.encode(event.name) + "/" + event.token)
  }

}
