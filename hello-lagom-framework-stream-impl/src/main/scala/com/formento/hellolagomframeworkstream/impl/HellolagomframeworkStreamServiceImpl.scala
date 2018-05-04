package com.formento.hellolagomframeworkstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.formento.hellolagomframeworkstream.api.HellolagomframeworkStreamService
import com.formento.hellolagomframework.api.HellolagomframeworkService

import scala.concurrent.Future

/**
  * Implementation of the HellolagomframeworkStreamService.
  */
class HellolagomframeworkStreamServiceImpl(hellolagomframeworkService: HellolagomframeworkService) extends HellolagomframeworkStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(hellolagomframeworkService.hello(_).invoke()))
  }
}
