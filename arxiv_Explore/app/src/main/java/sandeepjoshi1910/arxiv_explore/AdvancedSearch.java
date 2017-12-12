package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sandeepjoshi1910.arxiv_explore.Utilities.ArticlesRetriever;
import sandeepjoshi1910.arxiv_explore.Utilities.NetworkHelper;

public class AdvancedSearch extends AppCompatActivity {

    protected Button addNewFilter;
    protected LinearLayout mainLayout;


    protected Spinner selSpinOne;
    protected Spinner selSpinTwo;
    protected Spinner selSpinThree;

    protected Spinner joinSpinnerOne;
    protected Spinner joinSpinnerTwo;

    protected EditText filter_one_text;
    protected EditText filter_two_text;
    protected EditText filter_three_text;

    protected Button search_now;

    protected String starterURL = "https://export.arxiv.org/api/query?search_query=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);


        selSpinOne = (Spinner) findViewById(R.id.filter_one);
        selSpinTwo = (Spinner) findViewById(R.id.filter_two);
        selSpinThree = (Spinner) findViewById(R.id.filter_three);

        filter_one_text = (EditText) findViewById(R.id.filter_one_text);
        filter_two_text = (EditText) findViewById(R.id.filter_two_text);
        filter_three_text = (EditText) findViewById(R.id.filter_three_text);

        joinSpinnerOne = (Spinner) findViewById(R.id.join_one);
        joinSpinnerTwo = (Spinner) findViewById(R.id.join_two);

        search_now = (Button) findViewById(R.id.ad_search_now);


        selSpinOne.setSelection(0);
        selSpinTwo.setSelection(1);
        selSpinThree.setSelection(2);



        search_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!NetworkHelper.hasNetworkAccess(getApplicationContext())) {
                    Log.i("Click", "onClick: No Network Access... Can't get articles");
                    Toast.makeText(getApplicationContext(),"Check your network connection and retry",Toast.LENGTH_SHORT);
                    return;
                }

                String finalSearchURL = "" + starterURL;

                String filter1 = getSearchID(selSpinOne.getSelectedItemPosition());
                String filter2 = getSearchID(selSpinTwo.getSelectedItemPosition());
                String filter3 = getSearchID(selSpinThree.getSelectedItemPosition());

                String filtertext1 = filter_one_text.getText().toString();
                String filtertext2 = filter_two_text.getText().toString();
                String filtertext3 = filter_three_text.getText().toString();

                Boolean joinOne = joinSpinnerOne.getSelectedItemPosition() == 0;
                Boolean joinTwo = joinSpinnerTwo.getSelectedItemPosition() == 0;

                if(filtertext1.toString().length() > 1) {
                    finalSearchURL = finalSearchURL + filter1 + filtertext1;
                }


                if(filtertext2.toString().length() > 1) {
                    if(filtertext1.toString().length() > 1) {
                        if(joinOne) {
                            finalSearchURL = finalSearchURL + "+AND+";
                        } else {
                            finalSearchURL = finalSearchURL + "+ANDNOT+";
                        }
                    }

                    finalSearchURL = finalSearchURL + filter2 + filtertext2;

                }

                if(filtertext3.toString().length() > 1) {
                    if(filtertext2.toString().length() > 1) {
                        if(joinTwo) {
                            finalSearchURL = finalSearchURL + "+AND+";
                        } else {
                            finalSearchURL = finalSearchURL + "+ANDNOT+";
                        }
                    } else if(filtertext1.toString().length() > 1) {
                        if(joinOne) {
                            finalSearchURL = finalSearchURL + "+AND+";
                        } else {
                            finalSearchURL = finalSearchURL + "+ANDNOT+";
                        }
                    }
                    finalSearchURL = finalSearchURL + filter3 + filtertext3;
                }

                Intent articlesRetIntent = new Intent(AdvancedSearch.this, ArticlesRetriever.class);
                articlesRetIntent.putExtra("SearchTerm",finalSearchURL);
                startActivity(articlesRetIntent);

            }
        });





//        addNewFilter = (Button)findViewById(R.id.add);
//        mainLayout = (LinearLayout) findViewById(R.id.lin_layout);

//        addNewFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                TextView tv = new TextView(getApplicationContext());
////                tv.setText("Good Evening");
////                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
////                mainLayout.addView(tv);
//
//                AlertDialog.Builder searchBuilder = new AlertDialog.Builder(AdvancedSearch.this);
//                View searchView = getLayoutInflater().inflate(R.layout.search_dialog,null);
//                searchBuilder.setView(searchView);
//                AlertDialog sDialog = searchBuilder.create();
//                sDialog.show();
//
////                Button submit_btn = searchView.findViewById(R.)
//
//            }
//        });



    }

    protected String getSearchID(int selection) {
        if(selection == 0) {
            return "ti:";
        } else if(selection == 1) {
            return "au:";
        } else {
            return "abs:";
        }
    }
}
