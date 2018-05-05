package com.formento.hellolagomframeworkstream.impl

import com.formento.hellolagomframework.api.HellolagomframeworkService
import com.formento.hellolagomframeworkstream.api.HellolagomframeworkStreamService
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

/**
  * Implementation of the HellolagomframeworkStreamService.
  */
class HellolagomframeworkStreamServiceImpl(hellolagomframeworkService: HellolagomframeworkService) extends HellolagomframeworkStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(hellolagomframeworkService.hello(_).invoke()))
  }
}
