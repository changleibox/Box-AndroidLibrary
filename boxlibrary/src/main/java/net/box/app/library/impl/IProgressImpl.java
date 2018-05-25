/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.impl;

import android.content.DialogInterface;

/**
 * Created by box on 2018/5/25.
 * <p>
 * 进度条
 */
public interface IProgressImpl {

    void show();

    void setOnCancelListener(DialogInterface.OnCancelListener l);

    void dismiss();
}
