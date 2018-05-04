package com.formento.hellolagomframeworkstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * The hello-lagom-framework stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the HellolagomframeworkStream service.
  */
trait HellolagomframeworkStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor = {
    import Service._

    named("hello-lagom-framework-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

