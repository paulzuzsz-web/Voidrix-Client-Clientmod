package gg.norisk.client.v2.serverstyling

private data class AspectRatioResult(u: Float, v: Float, sourceW: Int, sourceH: Int) {
   public final val u: Float
   public final val v: Float
   public final val sourceW: Int
   public final val sourceH: Int

   init {
      this.u = u
      this.v = v
      this.sourceW = sourceW
      this.sourceH = sourceH
   }

   public operator fun component1(): Float {
      return this.u
   }

   public operator fun component2(): Float {
      return this.v
   }

   public operator fun component3(): Int {
      return this.sourceW
   }

   public operator fun component4(): Int {
      return this.sourceH
   }

   public fun copy(u: Float = this.u, v: Float = this.v, sourceW: Int = this.sourceW, sourceH: Int = this.sourceH): AspectRatioResult {
      return AspectRatioResult(u, v, sourceW, sourceH)
   }

   public override fun toString(): String {
      return "AspectRatioResult(u=${this.u}, v=${this.v}, sourceW=${this.sourceW}, sourceH=${this.sourceH})"
   }

   public override fun hashCode(): Int {
      return ((java.lang.Float.hashCode(this.u) * 31 + java.lang.Float.hashCode(this.v)) * 31 + Integer.hashCode(this.sourceW)) * 31
         + Integer.hashCode(this.sourceH)
      }

   public override operator fun equals(other: Any?): Boolean {
      label40@
      if (this === other) {
         return true
      } else {
         return other is AspectRatioResult
            && java.lang.Float.compare(this.u, (other as AspectRatioResult).u) == 0
            && java.lang.Float.compare(this.v, (other as AspectRatioResult).v) == 0
            && this.sourceW == (other as AspectRatioResult).sourceW
            && this.sourceH == (other as AspectRatioResult).sourceH
         }
   }
}
