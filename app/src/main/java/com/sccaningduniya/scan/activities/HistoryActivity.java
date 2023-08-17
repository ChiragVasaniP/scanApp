package com.sccaningduniya.scan.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.handlers.ButtonHandler;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.sccaningduniya.scan.adapters.HistoryAdapter;
import com.sccaningduniya.scan.database.HistoryEntity;
import com.sccaningduniya.scan.repository.HistoryViewModel;

import java.util.List;

@SuppressWarnings("ALL")

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "History";
    final Activity activity = this;
    private final HistoryAdapter adapter = new HistoryAdapter();
    private LiveData<List<HistoryEntity>> allItemsInHistory;
    private GeneralHandler generalHandler;
    private HistoryViewModel historyViewModel;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        this.generalHandler = generalHandler;
        generalHandler.loadTheme();
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_barcode_generate);
        setContentView(R.layout.activity_history);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.adapter);
        HistoryViewModel historyViewModel = (HistoryViewModel) new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(HistoryViewModel.class);
        this.historyViewModel = historyViewModel;
        historyViewModel.getAllItemsInHistory().observe(this, new Observer<List<HistoryEntity>>() { 
            @Override 
            public void onChanged(List<HistoryEntity> historyEntities) {
                adapter.submitList(historyEntities);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 12) { 
            @Override 
            public boolean onMove(RecyclerView recyclerView2, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override 
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction != 4) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    Intent intent = new Intent(activity, HistoryDetailsActivity.class);
                    intent.putExtra(HistoryDetailsActivity.EXTRA_FORMAT, adapter.getHistoryEntryAt(viewHolder.getAdapterPosition()).getFormat());
                    intent.putExtra(HistoryDetailsActivity.EXTRA_INFORMATION, adapter.getHistoryEntryAt(viewHolder.getAdapterPosition()).getInformation());
                    startActivity(intent);
                    return;
                }
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HistoryActivity.this);
                dialogBuilder.setMessage(R.string.delete_entry_history_dialog_message);
                dialogBuilder.setPositiveButton(R.string.delete_entry_history_confirmation, new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int i) {
                        historyViewModel.delete(adapter.getHistoryEntryAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(activity, activity.getResources().getText(R.string.notice_deleted_from_database), 0).show();
                    }
                });
                dialogBuilder.setNegativeButton(R.string.delete_entry_history_cancel, new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                dialogBuilder.show();
            }
        }).attachToRecyclerView(recyclerView);
        this.adapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() { 
            @Override 
            public void onItemClick(HistoryEntity historyEntry) {
                Intent intent = new Intent(activity, HistoryDetailsActivity.class);
                intent.putExtra(HistoryDetailsActivity.EXTRA_FORMAT, historyEntry.getFormat());
                intent.putExtra(HistoryDetailsActivity.EXTRA_INFORMATION, historyEntry.getInformation());
                startActivity(intent);
            }
        });
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_optionsmenu, menu);
        return true;
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_optionsmenu_delete :
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage(R.string.delete_history_dialog_message);
                dialogBuilder.setPositiveButton(R.string.delete_history_dialog_confirmation, new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int i) {
                        historyViewModel.deleteAllInHistory();
                    }
                });
                dialogBuilder.setNegativeButton(R.string.delete_history_dialog_cancel, new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialogBuilder.show();
                return true;
            case R.id.history_optionsmenu_share :
                this.allItemsInHistory = this.historyViewModel.getAllItemsInHistory();
                String export = "";
                for (int i = 0; i < this.adapter.getItemCount(); i++) {
                    export = (((export + "DATE:" + this.adapter.getHistoryEntryAt(i).getDate() + "\n") + "FORMAT:" + this.adapter.getHistoryEntryAt(i).getFormat() + "\n") + "INFO:" + this.adapter.getHistoryEntryAt(i).getInformation() + "\n") + "\n";
                }
                ButtonHandler.shareTo(export, this.activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
