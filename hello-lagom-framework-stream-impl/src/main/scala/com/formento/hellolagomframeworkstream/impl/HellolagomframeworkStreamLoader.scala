package com.formento.hellolagomframeworkstream.impl

import com.formento.hellolagomframework.api.HellolagomframeworkService
import com.formento.hellolagomframeworkstream.api.HellolagomframeworkStreamService
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class HellolagomframeworkStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new HellolagomframeworkStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new HellolagomframeworkStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[HellolagomframeworkStreamService])
}

abstract class HellolagomframeworkStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[HellolagomframeworkStreamService](wire[HellolagomframeworkStreamServiceImpl])

  // Bind the HellolagomframeworkService client
  lazy val hellolagomframeworkService = serviceClient.implement[HellolagomframeworkService]
}
