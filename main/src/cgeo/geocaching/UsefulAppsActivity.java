package cgeo.geocaching;

import butterknife.InjectView;
import butterknife.Views;

import cgeo.geocaching.activity.AbstractActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UsefulAppsActivity extends AbstractActivity {

    @InjectView(R.id.apps_list) protected ListView list;

    protected static class ViewHolder {
        @InjectView(R.id.title) protected TextView title;
        @InjectView(R.id.image) protected ImageView image;
        @InjectView(R.id.description) protected TextView description;

        public ViewHolder(View rowView) {
            Views.inject(this, rowView);
        }
    }

    private static class HelperApp {
        private final int titleId;
        private final int descriptionId;
        private final int iconId;
        private final String market;

        public HelperApp(final int title, final int description, final int icon, final String market) {
            this.titleId = title;
            this.descriptionId = description;
            this.iconId = icon;
            this.market = market;
        }

        private void installFromMarket(UsefulAppsActivity activity) {
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:" + market)));
            } catch (Exception e) {
                // market not available in standard emulator
            }

            activity.finish();
        }
    }

    private static final HelperApp[] HELPER_APPS = {
            new HelperApp(R.string.helper_calendar_title, R.string.helper_calendar_description, R.drawable.cgeo, "cgeo.calendar"),
            new HelperApp(R.string.helper_locus_title, R.string.helper_locus_description, R.drawable.helper_locus, "menion.android.locus"),
            new HelperApp(R.string.helper_gpsstatus_title, R.string.helper_gpsstatus_description, R.drawable.helper_gpsstatus, "com.eclipsim.gpsstatus2"),
            new HelperApp(R.string.helper_bluetoothgps_title, R.string.helper_bluetoothgps_description, R.drawable.helper_bluetoothgps, "googoo.android.btgps"),
            new HelperApp(R.string.helper_barcode_title, R.string.helper_barcode_description, R.drawable.helper_barcode, "com.google.zxing.client.android"),
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.useful_apps_activity);

        Views.inject(this);

        list.setAdapter(new ArrayAdapter<HelperApp>(this, R.layout.useful_apps_item, HELPER_APPS) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View rowView = convertView;
                if (null == rowView) {
                    rowView = getLayoutInflater().inflate(R.layout.useful_apps_item, null);
                }
                ViewHolder holder = (ViewHolder) rowView.getTag();
                if (null == holder) {
                    holder = new ViewHolder(rowView);
                    rowView.setTag(holder);
                }

                final HelperApp app = getItem(position);
                fillViewHolder(holder, app);
                return rowView;
            }

            private void fillViewHolder(ViewHolder holder, HelperApp app) {
                holder.title.setText(res.getString(app.titleId));
                holder.image.setImageDrawable(res.getDrawable(app.iconId));
                holder.description.setText(res.getString(app.descriptionId));
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HelperApp helperApp = HELPER_APPS[position];
                helperApp.installFromMarket(UsefulAppsActivity.this);
            }
        });
    }
}
