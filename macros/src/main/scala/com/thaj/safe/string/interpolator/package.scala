package com.thaj.safe.string

import com.thaj.safe.string.interpolator.instances.PrimitiveInstances

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

package object interpolator extends PrimitiveInstances {
  implicit class SafeStringContext(val sc: StringContext) {
    def safeStr(args: Any*): SafeString = macro Macro.impl
    def ss(args: Any*): SafeString = macro Macro.impl
  }

  implicit class AsString[T: Safe](s: => T) {
    def asStr: String = Safe[T].value(s)
  }

  object Macro {
    def impl(c: blackbox.Context)(args: c.Expr[Any]*): c.Expr[SafeString] = {
      import c.universe.{ Name => _, _ }

      c.prefix.tree match {

        case Apply(_, List(Apply(_, partz))) =>
          val parts: Seq[String] = partz map { case Literal(Constant(const: String)) => const }

          val res: c.universe.Tree =
            args
              .toList
              .foldLeft(q"""StringContext.apply(..$parts)""")({
                (acc, t) =>
                  val nextElement = t.tree
                  val tag = c.WeakTypeTag(nextElement.tpe)

                  if (nextElement.toString().contains(".toString"))
                    c.abort(
                      t.tree.pos,
                      s"Identified `toString` being called on the types. Make sure the type has a instance of Safe."
                    )

                  val field =
                    q"""com.thaj.safe.string.interpolator.Safe[${tag.tpe}].value($nextElement)"""

                  acc match {
                    case q"""StringContext.apply(..$raw ).s(..$previousElements ) """ =>
                      q"""StringContext.apply(..$raw).s(($previousElements :+ ..$field) :_*)"""
                    case _ => q"""$acc.s(..$field)"""
                  }
              })

          res match {
            case q"""StringContext.apply(..$raw ).s(..$previousElements ) """ =>
              c.Expr(q"""com.thaj.safe.string.interpolator.SafeString($res)""")
            case q"""StringContext.apply($raw ) """ =>
              c.Expr(q"""com.thaj.safe.string.interpolator.SafeString($raw)""")
          }

        case _ =>
          c.abort(c.prefix.tree.pos, "The pattern can't be used with the safeStr interpolation.")

      }
    }
  }
}
