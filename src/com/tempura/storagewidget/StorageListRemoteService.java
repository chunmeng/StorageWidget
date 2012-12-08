package com.tempura.storagewidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StorageListRemoteService extends RemoteViewsService {
      public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent)
      {
          return new StorageListAdapter(this, intent);
      }
}