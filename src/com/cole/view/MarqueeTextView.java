package cole.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;

public class MarqueeTextView extends AppCompatTextView {

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

}
