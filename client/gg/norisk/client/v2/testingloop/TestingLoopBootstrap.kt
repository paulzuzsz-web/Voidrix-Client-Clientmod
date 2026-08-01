package gg.norisk.client.v2.testingloop

public object TestingLoopBootstrap {
   public fun init() {
      TestingLoopCommand.INSTANCE.register()
   }
}
