package dal

import collection.mutable.{HashMap, ListBuffer}

case class Transaction(from: User, to: User, amount: BigDecimal)

// @Author: Jaap Taal

object Algorithms {

  /*
  * principe: splits op in gebruikers die moeten betalen en gebruikers
  * die gaan ontvangen, lijst is gesorteerd
  *
  * de gebruikers die moeten betalen bij elkaar heffen de gebruikers die
  * moeten ontvangen bij elkaar op.
  */


  def makeMatch(users: Seq[User], payments: Seq[Payment]): Seq[Transaction] = {

    val balances = getSortedUserBalances(users, payments)
    val payers = balances.filter(_._2 < 0)
    val receivers = collection.mutable.Map(balances.filter(_._2 > 0): _*)

    payers.flatMap(payer => {
      var amount = payer._2
      receivers.toSeq.sortBy(- _._2).flatMap(receiver => {
        math.min(-amount.toDouble, receiver._2.toDouble) match {
          case realAmount if realAmount > 0 =>
            amount = amount + realAmount
            receivers(receiver._1) -= realAmount
            Some(Transaction(payer._1, receiver._1, BigDecimal(realAmount)))
          case _ =>
            None
        }
      })
    })
  }

  def getSortedUserBalances(users: Seq[User], payments: Seq[Payment]): Seq[(User, BigDecimal)] = {
    val balance = collection.mutable.Map(users.map(u => (u, BigDecimal(0))): _*)

    payments.foreach(payment => {
      payment.forWhoIds.foreach(uid => {
        val forSomebody = users.find(_.id() == uid).get
        balance(forSomebody) += BigDecimal(- payment.amount / payment.forWhoIds.size)
      })

      val payer = users.find(_.id() == payment.payer_id).get
      balance(payer) += BigDecimal(payment.amount)
    })

    balance.toSeq.sortBy(_._2)
  }

}