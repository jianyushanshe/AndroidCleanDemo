package com.example.myapplication;

public abstract class TimeController {
    abstract long a(int paramInt1, int paramInt2, long paramLong);

    public final void b(int paramInt1, int paramInt2, long paramLong) {
        paramLong = a(paramInt1, paramInt2, paramLong) - paramLong;
        if (paramLong > 0L)
            try {
                Thread.sleep(paramLong);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
    }

    public static class MoreTimeController extends TimeController {
        private int a;

        private long b;

        private long c;

        public MoreTimeController(int param1Int, long param1Long1, long param1Long2) {
            this.a = param1Int;
            this.b = param1Long1;
            this.c = param1Long2;
        }

        long a(int param1Int1, int param1Int2, long param1Long) {
            // Byte code:
            //   0: lload_3
            //   1: aload_0
            //   2: getfield c : J
            //   5: lcmp
            //   6: ifle -> 11
            //   9: lconst_0
            //   10: lreturn
            //   11: aload_0
            //   12: getfield a : I
            //   15: i2l
            //   16: lload_3
            //   17: lmul
            //   18: lstore #5
            //   20: lload #5
            //   22: lstore #7
            //   24: iload_1
            //   25: ifle -> 43
            //   28: lload #5
            //   30: l2d
            //   31: ldc2_w 0.9
            //   34: iload_1
            //   35: i2d
            //   36: invokestatic pow : (DD)D
            //   39: dmul
            //   40: d2l
            //   41: lstore #7
            //   43: aload_0
            //   44: getfield b : J
            //   47: lstore #5
            //   49: lload #7
            //   51: lload #5
            //   53: lcmp
            //   54: ifge -> 68
            //   57: lload #5
            //   59: lstore #7
            //   61: lload #7
            //   63: lstore #5
            //   65: goto -> 93
            //   68: aload_0
            //   69: getfield c : J
            //   72: lstore #9
            //   74: lload #7
            //   76: lstore #5
            //   78: lload #7
            //   80: lload #9
            //   82: lcmp
            //   83: ifle -> 93
            //   86: lload #9
            //   88: lstore #7
            //   90: goto -> 61
            //   93: lload #5
            //   95: lload_3
            //   96: lcmp
            //   97: ifle -> 105
            //   100: lload #5
            //   102: lload_3
            //   103: lsub
            //   104: lreturn
            //   105: lconst_0
            //   106: lreturn
            return param1Long;
        }
    }
}
