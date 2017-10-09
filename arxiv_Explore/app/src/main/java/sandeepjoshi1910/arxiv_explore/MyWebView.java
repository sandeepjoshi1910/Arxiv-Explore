package sandeepjoshi1910.arxiv_explore;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;


public class MyWebView extends WebView{

    public MyWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    private long lastMoveEventTime = -1;
    private int eventTimeInterval = 40;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        long eventTime = ev.getEventTime();
        int action = ev.getAction();

        switch (action){
            case MotionEvent.ACTION_MOVE: {
                if ((eventTime - lastMoveEventTime) > eventTimeInterval){
                    lastMoveEventTime = eventTime;
                    return super.onTouchEvent(ev);
                }
                break;
            }
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP: {
                return super.onTouchEvent(ev);
            }
        }
        return true;
    }
}