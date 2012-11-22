package com.tempura.storagewidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class BetterStorageWidget extends AppWidgetProvider {
	public static String ACTION_WIDGET_CLICKED = "ActionWidgetClicked";
	
	public static void updateAppWidget(Context paramContext, AppWidgetManager paramAppWidgetManager, int paramInt) {
		
	}
	
	public void onEnabled(Context paramContext) {
		super.onEnabled(paramContext);
	}

	public void onReceive(Context paramContext, Intent paramIntent) {
		super.onReceive(paramContext, paramIntent);
	}
	
	public void onUpdate(Context paramContext, AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt) {
		
	}
}
