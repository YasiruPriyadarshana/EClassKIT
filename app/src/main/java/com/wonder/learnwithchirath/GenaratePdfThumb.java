package com.wonder.learnwithchirath;


import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;

public class GenaratePdfThumb {
    private   Bitmap bitmaps;
    public Bitmap pdfToBitmap(File pdfFile) {


        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));


            final int pageCount = renderer.getPageCount();

            PdfRenderer.Page page = renderer.openPage(0);
            Bitmap bitmap;
            int width = page.getWidth();
            int height = page.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            bitmaps=bitmap;

            // close the page
            page.close();


            // close the renderer
            renderer.close();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return bitmaps;
    }


}


