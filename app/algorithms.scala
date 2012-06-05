package algorithms

import dal.{Payment, User}


object Algorithms {

  def makeUpBalance(participants: Seq[User], payments: Seq[Payment]): Seq[(User, BigDecimal)] = {
    participants.map(p => (p, BigDecimal(10)))
  }



}

