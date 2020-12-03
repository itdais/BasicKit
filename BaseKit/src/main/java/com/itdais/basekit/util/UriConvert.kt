package com.itdais.basekit.util

import android.net.Uri
import android.provider.ContactsContract

/**
 * Author:  ding.jw
 * Description:
 * Uri转换工具类
 */
/**
 * Uri转换
 */
fun Uri.getContact(): Array<String?> {
    val result = arrayOfNulls<String>(2)
    Utils.context?.let {
        it.contentResolver.apply {
            val cursor = query(this@getContact, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                result[0] =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhone =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                //有电话
                if (hasPhone > 0) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val phones = query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null
                    )
                    if (phones != null && phones.moveToFirst()) {
                        result[1] =
                            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        phones.close()
                    }
                }
                cursor.close()
            }
        }
    }
    return result
}

