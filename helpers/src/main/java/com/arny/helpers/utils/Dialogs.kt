package com.arny.helpers.utils

import android.content.Context
import android.graphics.Color
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog


@JvmOverloads
fun alertDialog(context: Context, title: String, content: String? = null, btnOkText: String? = "OK", btnCancelText: String? = null, cancelable: Boolean = false, onConfirm: () -> Unit? = {}, onCancel: () -> Unit? = {}, alert: Boolean = true, autoDissmiss: Boolean = true, btnNeutralText: String? = null, onNeutral: () -> Unit? = {}): MaterialDialog? {
    if (!checkContextTheme(context)) return null
    val builder = MaterialDialog.Builder(context)
    builder.title(title)
    builder.titleColor(if (alert) Color.RED else Color.BLACK)
    builder.cancelable(cancelable)
    builder.contentColor(Color.BLACK)
    if (btnOkText != null) {
        builder.positiveText(btnOkText)
        builder.onPositive { dialog, _ ->
            if (autoDissmiss) {
                dialog.dismiss()
            }
            onConfirm.invoke()
        }
    }
    if (btnCancelText != null) {
        builder.negativeText(btnCancelText)
        builder.onNegative { dialog, _ ->
            if (autoDissmiss) {
                dialog.dismiss()
            }
            onCancel.invoke()
        }
    }
    if (btnNeutralText != null) {
        builder.neutralText(btnNeutralText)
        builder.onNeutral { dialog, _ ->
            if (autoDissmiss) {
                dialog.dismiss()
            }
            onNeutral.invoke()
        }
    }
    if (!content.isNullOrBlank()) {
        builder.content(fromHtml(content))
    }
    val dlg = builder.build()
    dlg.show()
    return dlg

}


@JvmOverloads
fun listDialog(context: Context, vararg items: String, title: String? = null, cancelable: Boolean = false, dialogListener: ListDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title.toString())
            .cancelable(cancelable)
            .items(items.toString())
            .itemsCallback { _, _, which, _ -> dialogListener?.onClick(which) }
            .build()
    dlg.show()
}

@JvmOverloads
fun listDialog(context: Context, title: String? = null, content: String? = null, items: List<String>, cancelable: Boolean? = false, onChoose: (position: Int, title: CharSequence) -> Unit): MaterialDialog? {
    val builder = MaterialDialog.Builder(context)
            .cancelable(cancelable ?: false)
    if (!title.isNullOrBlank()) {
        builder.title(title)
    }
    if (!content.isNullOrBlank()) {
        builder.content(content)
    }
    builder.items(items)
    val dlg = builder
            .itemsCallback { _, _, position, text -> onChoose.invoke(position, text) }
            .build()
    dlg.show()
    return dlg
}


@JvmOverloads
fun checkDialog(context: Context, title: String? = null, items: List<String>, itemsSelected: Array<Int>? = null, cancelable: Boolean = false, onChoose: (which: Array<Int>) -> Unit): MaterialDialog? {
    val builder = MaterialDialog.Builder(context)
            .cancelable(cancelable)
    if (!title.isNullOrBlank()) {
        builder.title(title)
    }
    builder.items(items)
    val dlg = builder.itemsCallbackMultiChoice(itemsSelected) { _, which, _ ->
        onChoose.invoke(which)
        true
    }
            .positiveText(android.R.string.ok)
            .build()
    dlg.show()
    return dlg
}


@JvmOverloads
fun confirmDialog(context: Context, title: String, content: String? = null, btnOkText: String? = context.getString(android.R.string.ok), btnCancelText: String? = context.getString(android.R.string.cancel), cancelable: Boolean = false, dialogListener: ConfirmDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title)
            .cancelable(cancelable)
            .positiveText(btnOkText.toString())
            .negativeText(btnCancelText.toString())
            .onPositive { _, _ -> dialogListener?.onConfirm() }
            .onNegative { _, _ -> dialogListener?.onCancel() }
            .build()
    if (content != null) {
        dlg.setContent(content)
    }
    dlg.show()
}

@JvmOverloads
fun inputDialog(context: Context?, title: String? = null, hint: String? = null, content: String? = null, prefill: String? = null, btnOkText: String? = null, btnCancelText: String? = null, cancelable: Boolean? = null, inputType: Int? = null, onInput: (content: CharSequence?) -> Unit?, onCancel: () -> Unit? = {}): MaterialDialog? {
    if (context == null) return null
    if (!checkContextTheme(context)) return null
    val builder = MaterialDialog.Builder(context)
    if (!title.isNullOrBlank()) {
        builder.title(title)
    }
    if (!content.isNullOrBlank()) {
        builder.content(content)
    }
    builder.cancelable(cancelable ?: false)
            .positiveText(btnOkText ?: "OK")
            .negativeText(btnCancelText ?: "Отмена")
            .inputType(inputType ?: InputType.TYPE_CLASS_TEXT)
            .onNegative { dialog, _ ->
                dialog.dismiss()
                onCancel.invoke()
            }
            .input(hint ?: "", prefill ?: "") { dialog, input ->
                dialog.dismiss()
                onInput.invoke(input)
            }
    val dlg = builder.build()
    dlg.show()
    return dlg
}

