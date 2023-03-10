package com.example.myapplication.cache;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

public class FileUtil {
    private static void a(File paramFile) {
        if (paramFile == null)
            return;
        if (paramFile.isDirectory()) {
            File[] arrayOfFile = paramFile.listFiles();
            if (arrayOfFile == null)
                return;
            for (byte b = 0; b < arrayOfFile.length; b++)
                a(arrayOfFile[b]);
            paramFile.delete();
        } else if (paramFile.exists()) {
            paramFile.delete();
        }
    }

    protected static void b(String paramString) {
        if (TextUtils.isEmpty(paramString))
            return;
        a(new File(paramString));
    }

    protected static long c(String paramString) {
        long l;
        File file = new File(paramString);
        try {
            if (file.isDirectory()) {
                l = e(file);
            } else {
                l = d(file);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            l = 0L;
        }
        return l;
    }

    private static long d(File paramFile) {
        // Byte code:
        //   0: aconst_null
        //   1: astore_1
        //   2: aconst_null
        //   3: astore_2
        //   4: aconst_null
        //   5: astore_3
        //   6: aconst_null
        //   7: astore #4
        //   9: aconst_null
        //   10: astore #5
        //   12: aconst_null
        //   13: astore #6
        //   15: aconst_null
        //   16: astore #7
        //   18: aconst_null
        //   19: astore #8
        //   21: aconst_null
        //   22: astore #9
        //   24: lconst_0
        //   25: lstore #10
        //   27: aload_0
        //   28: invokevirtual exists : ()Z
        //   31: ifeq -> 156
        //   34: new java/io/FileInputStream
        //   37: astore #12
        //   39: aload #12
        //   41: aload_0
        //   42: invokespecial <init> : (Ljava/io/File;)V
        //   45: aload #9
        //   47: astore #4
        //   49: aload_1
        //   50: astore #5
        //   52: aload_2
        //   53: astore #6
        //   55: aload #7
        //   57: astore_0
        //   58: aload #12
        //   60: astore #7
        //   62: aload #12
        //   64: invokevirtual getChannel : ()Ljava/nio/channels/FileChannel;
        //   67: astore #8
        //   69: aload #8
        //   71: ifnull -> 107
        //   74: aload #8
        //   76: astore #4
        //   78: aload #8
        //   80: astore #5
        //   82: aload #8
        //   84: astore #6
        //   86: aload #8
        //   88: astore_0
        //   89: aload #12
        //   91: astore #7
        //   93: aload #8
        //   95: invokevirtual size : ()J
        //   98: lstore #13
        //   100: lload #13
        //   102: lstore #10
        //   104: goto -> 170
        //   107: aload #8
        //   109: astore #4
        //   111: aload #8
        //   113: astore #5
        //   115: aload #8
        //   117: astore #6
        //   119: aload #8
        //   121: astore_0
        //   122: aload #12
        //   124: astore #7
        //   126: aload #12
        //   128: invokevirtual available : ()I
        //   131: istore #15
        //   133: iload #15
        //   135: i2l
        //   136: lstore #10
        //   138: goto -> 170
        //   141: astore #8
        //   143: goto -> 237
        //   146: astore #8
        //   148: goto -> 297
        //   151: astore #8
        //   153: goto -> 357
        //   156: ldc '
        //   158: ldc '
        //   160: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
        //   163: pop
        //   164: aconst_null
        //   165: astore #12
        //   167: aload_3
        //   168: astore #8
        //   170: aload #8
        //   172: ifnull -> 188
        //   175: aload #8
        //   177: invokevirtual close : ()V
        //   180: goto -> 188
        //   183: astore_0
        //   184: aload_0
        //   185: invokevirtual printStackTrace : ()V
        //   188: lload #10
        //   190: lstore #13
        //   192: aload #12
        //   194: ifnull -> 409
        //   197: lload #10
        //   199: lstore #13
        //   201: aload #12
        //   203: invokevirtual close : ()V
        //   206: lload #10
        //   208: lstore #13
        //   210: goto -> 409
        //   213: astore_0
        //   214: aload_0
        //   215: invokevirtual printStackTrace : ()V
        //   218: goto -> 409
        //   221: astore #12
        //   223: aconst_null
        //   224: astore #7
        //   226: aload #8
        //   228: astore_0
        //   229: goto -> 414
        //   232: astore #8
        //   234: aconst_null
        //   235: astore #12
        //   237: aload #4
        //   239: astore_0
        //   240: aload #12
        //   242: astore #7
        //   244: aload #8
        //   246: invokevirtual printStackTrace : ()V
        //   249: aload #4
        //   251: ifnull -> 267
        //   254: aload #4
        //   256: invokevirtual close : ()V
        //   259: goto -> 267
        //   262: astore_0
        //   263: aload_0
        //   264: invokevirtual printStackTrace : ()V
        //   267: lload #10
        //   269: lstore #13
        //   271: aload #12
        //   273: ifnull -> 409
        //   276: lload #10
        //   278: lstore #13
        //   280: aload #12
        //   282: invokevirtual close : ()V
        //   285: lload #10
        //   287: lstore #13
        //   289: goto -> 409
        //   292: astore #8
        //   294: aconst_null
        //   295: astore #12
        //   297: aload #5
        //   299: astore_0
        //   300: aload #12
        //   302: astore #7
        //   304: aload #8
        //   306: invokevirtual printStackTrace : ()V
        //   309: aload #5
        //   311: ifnull -> 327
        //   314: aload #5
        //   316: invokevirtual close : ()V
        //   319: goto -> 327
        //   322: astore_0
        //   323: aload_0
        //   324: invokevirtual printStackTrace : ()V
        //   327: lload #10
        //   329: lstore #13
        //   331: aload #12
        //   333: ifnull -> 409
        //   336: lload #10
        //   338: lstore #13
        //   340: aload #12
        //   342: invokevirtual close : ()V
        //   345: lload #10
        //   347: lstore #13
        //   349: goto -> 409
        //   352: astore #8
        //   354: aconst_null
        //   355: astore #12
        //   357: aload #6
        //   359: astore_0
        //   360: aload #12
        //   362: astore #7
        //   364: aload #8
        //   366: invokevirtual printStackTrace : ()V
        //   369: aload #6
        //   371: ifnull -> 387
        //   374: aload #6
        //   376: invokevirtual close : ()V
        //   379: goto -> 387
        //   382: astore_0
        //   383: aload_0
        //   384: invokevirtual printStackTrace : ()V
        //   387: lload #10
        //   389: lstore #13
        //   391: aload #12
        //   393: ifnull -> 409
        //   396: lload #10
        //   398: lstore #13
        //   400: aload #12
        //   402: invokevirtual close : ()V
        //   405: lload #10
        //   407: lstore #13
        //   409: lload #13
        //   411: lreturn
        //   412: astore #12
        //   414: aload_0
        //   415: ifnull -> 430
        //   418: aload_0
        //   419: invokevirtual close : ()V
        //   422: goto -> 430
        //   425: astore_0
        //   426: aload_0
        //   427: invokevirtual printStackTrace : ()V
        //   430: aload #7
        //   432: ifnull -> 448
        //   435: aload #7
        //   437: invokevirtual close : ()V
        //   440: goto -> 448
        //   443: astore_0
        //   444: aload_0
        //   445: invokevirtual printStackTrace : ()V
        //   448: aload #12
        //   450: athrow
        // Exception table:
        //   from	to	target	type
        //   27	45	352	java/io/FileNotFoundException
        //   27	45	292	java/io/IOException
        //   27	45	232	java/lang/Exception
        //   27	45	221	finally
        //   62	69	151	java/io/FileNotFoundException
        //   62	69	146	java/io/IOException
        //   62	69	141	java/lang/Exception
        //   62	69	412	finally
        //   93	100	151	java/io/FileNotFoundException
        //   93	100	146	java/io/IOException
        //   93	100	141	java/lang/Exception
        //   93	100	412	finally
        //   126	133	151	java/io/FileNotFoundException
        //   126	133	146	java/io/IOException
        //   126	133	141	java/lang/Exception
        //   126	133	412	finally
        //   156	164	352	java/io/FileNotFoundException
        //   156	164	292	java/io/IOException
        //   156	164	232	java/lang/Exception
        //   156	164	221	finally
        //   175	180	183	java/io/IOException
        //   201	206	213	java/io/IOException
        //   244	249	412	finally
        //   254	259	262	java/io/IOException
        //   280	285	213	java/io/IOException
        //   304	309	412	finally
        //   314	319	322	java/io/IOException
        //   340	345	213	java/io/IOException
        //   364	369	412	finally
        //   374	379	382	java/io/IOException
        //   400	405	213	java/io/IOException
        //   418	422	425	java/io/IOException
        //   435	440	443	java/io/IOException
        return 0;
    }

    private static long e(File paramFile) {
        File[] arrayOfFile = paramFile.listFiles();
        long l = 0L;
        if (arrayOfFile == null)
            return 0L;
        for (byte b = 0; b < arrayOfFile.length; b++) {
            long l1;
            if (arrayOfFile[b].isDirectory()) {
                l1 = e(arrayOfFile[b]);
            } else {
                l1 = d(arrayOfFile[b]);
            }
            l += l1;
        }
        return l;
    }

    protected static long f(Context paramContext) {
        try {
            File file = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(file.getPath());
            long l = statFs.getBlockSize();
            int i = statFs.getAvailableBlocks();
            return l * i;
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1L;
        }
    }

    protected static long g(Context paramContext) {
        try {
            File file = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(file.getPath());
            long l = statFs.getBlockSize();
            int i = statFs.getBlockCount();
            return l * i;
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1L;
        }
    }

    public static boolean h(String paramString) {
        return TextUtils.isEmpty(paramString) ? false : ((new File(paramString)).exists());
    }
}
