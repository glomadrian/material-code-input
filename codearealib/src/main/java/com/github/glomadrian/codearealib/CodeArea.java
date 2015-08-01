// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

package com.github.glomadrian.codearealib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeArea extends View {

  private static final int DEFAULT_CODES = 6;
  private static final Pattern KEYCODE_PATTERN = Pattern.compile("KEYCODE_(\\w)");
  private FixedStack characters;
  private SectionPath sections[];
  private Paint underlinePaint;
  private Paint underlineSelectedPaint;
  private Paint textPaint;
  private Paint hintPaint;
  private ValueAnimator reductionAnimator;
  private ValueAnimator hintYAnimator;
  private ValueAnimator hintSizeAnimator;
  private float sectionReduction;
  private float sectionStrokeWidth;
  private float sectionWidth;
  private float reduction;
  private float textSize;
  private float textMarginBottom;
  private float hintX;
  private float hintNormalSize;
  private float hintSmallSize;
  private float hintMarginBottom;
  private float hintActualMarginBottom;
  private long animationDuration;
  private int height;
  private int sectionsAmount;
  private int underlineColor;
  private boolean underlined = true;
  private String hintText;

  public CodeArea(Context context) {
    super(context);
    init(null);
  }

  public CodeArea(Context context, AttributeSet attributeset) {
    super(context, attributeset);
    init(attributeset);
  }

  public CodeArea(Context context, AttributeSet attributeset, int defStyledAttrs) {
    super(context, attributeset, defStyledAttrs);
    init(attributeset);
  }

  private SectionPath createPath(int position, float sectionWidth) {
    float fromX = sectionWidth * (float) position;
    return new SectionPath(fromX, height, fromX + sectionWidth, height);
  }

  private void init(AttributeSet attributeset) {
    initAttributes(attributeset);
    initPaint();
    initAnimator();
    initViewOptions();
  }

  private void initAttributes(AttributeSet attributeset) {
    sections = new SectionPath[sectionsAmount];
    characters = new FixedStack();
    characters.setMaxSize(sectionsAmount);
    sectionStrokeWidth = getContext().getResources().getDimension(R.dimen.underline_stroke_width);
    sectionWidth = getContext().getResources().getDimension(R.dimen.underline_width);
    sectionReduction = getContext().getResources().getDimension(R.dimen.section_reduction);
    textSize = getContext().getResources().getDimension(R.dimen.text_size);
    textMarginBottom = getContext().getResources().getDimension(R.dimen.text_margin_bottom);
    underlineColor = getContext().getResources().getColor(R.color.underline_default_color);
    hintMarginBottom = getContext().getResources().getDimension(R.dimen.hint_margin_bottom);
    hintNormalSize = getContext().getResources().getDimension(R.dimen.hint_size);
    hintSmallSize = getContext().getResources().getDimension(R.dimen.hint_small_size);
    animationDuration = getContext().getResources().getInteger(R.integer.animation_duration);
    hintText = " ";
    hintX = 0;
    hintActualMarginBottom = 0;
    sectionsAmount = DEFAULT_CODES;
    reduction = 0.0F;
  }

  private void initPaint() {
    underlinePaint = new Paint();
    underlinePaint.setColor(underlineColor);
    underlinePaint.setStrokeWidth(sectionStrokeWidth);
    underlinePaint.setStyle(android.graphics.Paint.Style.STROKE);
    underlineSelectedPaint = new Paint();
    underlineSelectedPaint.setStrokeWidth(sectionStrokeWidth);
    underlineSelectedPaint.setStyle(android.graphics.Paint.Style.STROKE);
    textPaint = new Paint();
    textPaint.setTextSize(textSize);
    textPaint.setAntiAlias(true);
    textPaint.setTextAlign(Paint.Align.CENTER);
    hintPaint = new Paint();
    hintPaint.setTextSize(hintNormalSize);
    hintPaint.setAntiAlias(true);
    hintPaint.setColor(underlineColor);
  }

  private void initAnimator() {
    reductionAnimator = ValueAnimator.ofFloat(0, sectionReduction);
    reductionAnimator.setDuration(animationDuration);
    reductionAnimator.addUpdateListener(new ReductionAnimatorListener());
    reductionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    hintSizeAnimator = ValueAnimator.ofFloat(hintNormalSize, hintSmallSize);
    hintSizeAnimator.setDuration(animationDuration);
    hintSizeAnimator.addUpdateListener(new HintSizeAnimatorListener());
    hintSizeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    hintYAnimator = ValueAnimator.ofFloat(0, hintMarginBottom);
    hintYAnimator.setDuration(animationDuration);
    hintYAnimator.addUpdateListener(new HintYAnimatorListener());
    hintYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
  }

  private void initViewOptions() {
    setFocusable(true);
    setFocusableInTouchMode(true);
  }

  @Override
  protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    if (!gainFocus && characters.size() == 0) {
      reverseAnimation();
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    height = h;
    initPaths();
  }

  private void initPaths() {
    for (int i = 0; i < sectionsAmount; i++) {
      sections[i] = createPath(i, sectionWidth);
    }
  }

  private void showKeyboard() {
    InputMethodManager inputmethodmanager =
        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputmethodmanager.toggleSoftInput(1, 0);
    inputmethodmanager.viewClicked(this);
  }

  private void startAnimation() {
    reductionAnimator.start();
    hintSizeAnimator.start();
    hintYAnimator.start();
  }

  private void reverseAnimation() {
    reductionAnimator.reverse();
    hintSizeAnimator.reverse();
    hintYAnimator.reverse();
    underlined = true;
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
    if (keyCode == 67 && characters.size() != 0) {
      characters.pop();
    }
    return super.onKeyDown(keyCode, keyevent);
  }

  @Override public boolean onKeyUp(int keyCode, KeyEvent keyevent) {
    String text = KeyEvent.keyCodeToString(keyCode);
    Matcher matcher = KEYCODE_PATTERN.matcher(text);
    if (matcher.matches()) {
      char character = matcher.group(1).charAt(0);
      characters.push(character);
      return true;
    } else {
      return false;
    }
  }

  @Override public boolean onTouchEvent(MotionEvent motionevent) {
    if (motionevent.getAction() == 0) {
      requestFocus();
      if (underlined) {
        startAnimation();
        underlined = false;
      }

      showKeyboard();
    }
    return super.onTouchEvent(motionevent);
  }

  @Override protected void onDraw(Canvas canvas) {
    for (int i = 0; i < sections.length; i++) {
      SectionPath sectionpath = sections[i];
      float fromX = sectionpath.fromX + reduction;
      float fromY = sectionpath.fromY;
      float toX = sectionpath.toX - reduction;
      float toY = sectionpath.toY;
      drawSection(i, fromX, fromY, toX, toY, canvas);
      if (characters.toArray().length > i && characters.size() != 0) {
        drawCharacter(fromX, toX, (Character) characters.get(i), canvas);
      }
    }
    drawHint(canvas);
    invalidate();
  }

  private void drawSection(int position, float fromX, float fromY, float toX, float toY,
      Canvas canvas) {
    Paint paint = underlinePaint;
    if (position == characters.size() && !underlined) {
      paint = underlineSelectedPaint;
    }
    canvas.drawLine(fromX, fromY, toX, toY, paint);
  }

  private void drawCharacter(float fromX, float toX, Character character, Canvas canvas) {
    float actualWidth = toX - fromX;
    float centerWidth = actualWidth / 2;
    float centerX = fromX + centerWidth;
    canvas.drawText(character.toString(), centerX, height - textMarginBottom, textPaint);
  }

  private void drawHint(Canvas canvas) {
    canvas.drawText(hintText, hintX, height - textMarginBottom - hintActualMarginBottom, hintPaint);
  }

  /**
   * Custom animator listener
   */
  private class ReductionAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

    public void onAnimationUpdate(ValueAnimator valueanimator) {
      float f = ((Float) valueanimator.getAnimatedValue()).floatValue();
      reduction = f;
    }
  }

  private class HintYAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

    @Override public void onAnimationUpdate(ValueAnimator animation) {
      hintActualMarginBottom = (float) animation.getAnimatedValue();
    }
  }

  private class HintSizeAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

    @Override public void onAnimationUpdate(ValueAnimator animation) {
      float size = (float) animation.getAnimatedValue();
      hintPaint.setTextSize(size);
    }
  }
}