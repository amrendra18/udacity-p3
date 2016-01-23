package it.jaschke.alexandria.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import it.jaschke.alexandria.utils.AppConstants;
import it.jaschke.alexandria.utils.FontsCache;


/**
 * Created by Amrendra Kumar on 21/12/15.
 */
public class BookContentTextView extends TextView {

    public BookContentTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public BookContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public BookContentTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontsCache.getTypeface(context, AppConstants.BOOK_CONTENT_FONT);
        setTypeface(customFont);
    }
}
