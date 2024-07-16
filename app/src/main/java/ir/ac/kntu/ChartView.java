package ir.ac.kntu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartView extends View {
    private Paint linePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private Paint gridPaint;
    private List<Entry> dataPoints;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.CYAN);
        linePaint.setStrokeWidth(4f);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(2f);
        gridPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.size() == 0) {
            return;
        }

        float maxBalance = getMaxBalance();
        float minBalance = getMinBalance();
        float chartHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float chartWidth = getWidth() - getPaddingLeft() - getPaddingRight();

        float xInterval = chartWidth / (dataPoints.size() - 1);
        float yScale = chartHeight / (maxBalance - minBalance);

        float[] values = new float[4];
        values[0] = maxBalance;
        values[1] = minBalance;
        values[2] = chartHeight;
        values[3] = chartWidth;
        drawGridLines(canvas, values);
        Path path = new Path();
        path.moveTo(getPaddingLeft(), getPaddingTop() + (maxBalance - dataPoints.get(0).getBalance()) * yScale);

        for (int i = 1; i < dataPoints.size(); i++) {
            float x = getPaddingLeft() + i * xInterval;
            float y = getPaddingTop() + (maxBalance - dataPoints.get(i).getBalance()) * yScale;
            path.lineTo(x, y);
        }

        canvas.drawPath(path, linePaint);

        // Draw data points and labels
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd", Locale.US);
        for (int i = 0; i < dataPoints.size(); i++) {
            float x = getPaddingLeft() + i * xInterval;
            float y = getPaddingTop() + (maxBalance - dataPoints.get(i).getBalance()) * yScale;
            canvas.drawCircle(x, y, 8, pointPaint);
            canvas.drawText(String.valueOf(dataPoints.get(i).getBalance()), x, y - 20, textPaint);
            canvas.drawText(dateFormat.format(dataPoints.get(i).getDate()), x, getHeight() - getPaddingBottom() + 20, textPaint);
        }
    }

    private void drawGridLines(Canvas canvas, float[] values) {
        float maxBalance = values[0];
        float minBalance = values[1];
        float chartHeight = values[2];
        float chartWidth = values[3];
        float yInterval = chartHeight / 5; // 5 horizontal grid lines
        for (int i = 0; i <= 5; i++) {
            float y = getPaddingTop() + i * yInterval;
            canvas.drawLine(getPaddingLeft(), y, getWidth() - getPaddingRight(), y, gridPaint);
            float label = maxBalance - i * (maxBalance - minBalance) / 5;
            canvas.drawText(String.valueOf((int) label), getPaddingLeft() - 30, y, textPaint);
        }

        float xInterval = chartWidth / (dataPoints.size() - 1);
        for (int i = 0; i < dataPoints.size(); i++) {
            float x = getPaddingLeft() + i * xInterval;
            canvas.drawLine(x, getPaddingTop(), x, getHeight() - getPaddingBottom(), gridPaint);
        }
    }

    private float getMaxBalance() {
        float max = Float.MIN_VALUE;
        for (Entry point : dataPoints) {
            if (point.getBalance() > max) {
                max = point.getBalance();
            }
        }
        return max;
    }

    private float getMinBalance() {
        float min = Float.MAX_VALUE;
        for (Entry point : dataPoints) {
            if (point.getBalance() < min) {
                min = point.getBalance();
            }
        }
        return min;
    }

    public void setDataPoints(List<Entry> dataPoints) {
        this.dataPoints = dataPoints;
        invalidate(); // Redraw the view
    }

    public static class Entry {
        private final Date date;
        private final float balance;

        public Entry(Date date, float balance) {
            this.date = date;
            this.balance = balance;
        }

        public Date getDate() {
            return date;
        }

        public float getBalance() {
            return balance;
        }
    }
}
