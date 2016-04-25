
package in.thyferny.images.core.codec.convertor;


public class SlowInverseDCTCalculator extends InverseDCTCalculator {

    public static final int PASS1_BITS      = 2;
    public static final int CONST_BITS      = 13;
    public static final int ONE             = 1;
    public static final int CONST_SCALE     = ONE << CONST_BITS;

    public static final int FIX_0_541196100 = 4433;
    public static final int FIX_2_562915447 = 20995;
    public static final int FIX_0_899976223 = 7373;
    public static final int FIX_0_765366865 = 6270;
    public static final int FIX_1_175875602 = 9633;
    public static final int FIX_0_298631336 = 2446;
    public static final int FIX_0_390180644 = 3196;
    public static final int FIX_1_501321110 = 12299;
    public static final int FIX_1_847759065 = 15137;
    public static final int FIX_1_961570560 = 16069;
    public static final int FIX_2_053119869 = 16819;
    public static final int FIX_3_072711026 = 25172;

    public static final int FIX_0_275899379 = FIX(0.275899379);
    public static final int FIX_1_306562965 = FIX(1.306562965);
    public static final int FIX_1_387039845 = FIX(1.387039845);
    public static final int FIX_0_601344887 = FIX(0.601344887);
    public static final int FIX_0_509795579 = FIX(0.509795579);
    public static final int FIX_1_353318001 = FIX(1.353318001);
    public static final int FIX_1_247225013 = FIX(1.247225013);
    public static final int FIX_1_093201867 = FIX(1.093201867);
    public static final int FIX_0_897167586 = FIX(0.897167586);
    public static final int FIX_0_666655658 = FIX(0.666655658);
    public static final int FIX_0_410524528 = FIX(0.410524528);
    public static final int FIX_2_286341144 = FIX(2.286341144);
    public static final int FIX_1_835730603 = FIX(1.835730603);
    public static final int FIX_0_138617169 = FIX(0.138617169);
    public static final int FIX_0_071888074 = FIX(0.071888074);
    public static final int FIX_1_125726048 = FIX(1.125726048);
    public static final int FIX_1_407403738 = FIX(1.407403738);
    public static final int FIX_0_766367282 = FIX(0.766367282);
    public static final int FIX_1_971951411 = FIX(1.971951411);
    public static final int FIX_1_065388962 = FIX(1.065388962);
    public static final int FIX_3_141271809 = FIX(3.141271809);

    
    @Override
    public Object calculate(int[] input, int inPos, int[] quant, int[] output, int outPos, int width, int height) {
        if (width == 16 && height == 16) {
            return calculate16x16(input, inPos, quant, output, outPos);
        } else if (width == 16 && height == 8) {
            return calculate16x8(input, inPos, quant, output, outPos);
        } else if (width == 8 && height == 16) {
            return calculate8x16(input, inPos, quant, output, outPos);
        } else if (width == 8 && height == 8) {
            return calculate8x8(input, inPos, quant, output, outPos);
        } else {
            throw new IllegalArgumentException("Unsupported output size!");
        }
    }

    public static int FIX(double x) {
        return (int) ((x) * CONST_SCALE + 0.5);
    }

    private int DESCALE(int x, int n) {
        return (x + (ONE << (n - 1))) >> n;
    }

    protected Object calculate16x16(int[] inptr, int inPos, int[] quantptr, int[] output, int outPos) {
        int tmp0, tmp1, tmp2, tmp3, tmp10, tmp11, tmp12, tmp13;
        int tmp20, tmp21, tmp22, tmp23, tmp24, tmp25, tmp26, tmp27;
        int z1, z2, z3, z4;

        int[] outptr = (output != null ? output : new int[DCTSIZE2 * 4]);

        short[] rangeLimitMap = sampleRangeLimitTable;
        int rangeLimitOffset = sampleRangeLimitOffset + CENTERJSAMPLE;

        int ctr;
        int workspace[] = new int[8 * 16]; 

        int inputOffset = inPos, quantOffset = 0, wsOffset = 0, outputOffset1 = 0, outputOffset2 = 0;

        
        int[] wsptr = workspace;

        for (ctr = 0; ctr < 8; ctr++, inputOffset++, quantOffset++, wsOffset++) {
            

            tmp0 = inptr[inputOffset] * quantptr[quantOffset];
            tmp0 <<= CONST_BITS;

            
            tmp0 += (1 << (CONST_BITS - PASS1_BITS - 1));

            z1 = inptr[inputOffset + DCTSIZE * 4] * quantptr[quantOffset + DCTSIZE * 4];
            tmp1 = (z1 * FIX_1_306562965); 
            tmp2 = (z1 * FIX_0_541196100); 

            tmp10 = tmp0 + tmp1;
            tmp11 = tmp0 - tmp1;
            tmp12 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;

            z1 = inptr[inputOffset + DCTSIZE * 2] * quantptr[quantOffset + DCTSIZE * 2];
            z2 = inptr[inputOffset + DCTSIZE * 6] * quantptr[quantOffset + DCTSIZE * 6];
            z3 = z1 - z2;
            z4 = (z3 * FIX_0_275899379); 
            z3 = (z3 * FIX_1_387039845); 

            tmp0 = z3 + (z2 * FIX_2_562915447);
            tmp1 = z4 + (z1 * FIX_0_899976223);
            tmp2 = z3 - (z1 * FIX_0_601344887);
            tmp3 = z4 - (z2 * FIX_0_509795579);

            tmp20 = tmp10 + tmp0;
            tmp27 = tmp10 - tmp0;
            tmp21 = tmp12 + tmp1;
            tmp26 = tmp12 - tmp1;
            tmp22 = tmp13 + tmp2;
            tmp25 = tmp13 - tmp2;
            tmp23 = tmp11 + tmp3;
            tmp24 = tmp11 - tmp3;

            

            z1 = inptr[inputOffset + DCTSIZE * 1] * quantptr[quantOffset + DCTSIZE * 1];
            z2 = inptr[inputOffset + DCTSIZE * 3] * quantptr[quantOffset + DCTSIZE * 3];
            z3 = inptr[inputOffset + DCTSIZE * 5] * quantptr[quantOffset + DCTSIZE * 5];
            z4 = inptr[inputOffset + DCTSIZE * 7] * quantptr[quantOffset + DCTSIZE * 7];

            tmp11 = z1 + z3;

            tmp1 = ((z1 + z2) * FIX_1_353318001); 
            tmp2 = (tmp11 * FIX_1_247225013); 
            tmp3 = ((z1 + z4) * FIX_1_093201867); 
            tmp10 = ((z1 - z4) * FIX_0_897167586); 
            tmp11 = (tmp11 * FIX_0_666655658); 
            tmp12 = ((z1 - z2) * FIX_0_410524528); 
            tmp0 = tmp1 + tmp2 + tmp3 - (z1 * FIX_2_286341144);
            tmp13 = tmp10 + tmp11 + tmp12 - (z1 * FIX_1_835730603);
            z1 = ((z2 + z3) * FIX_0_138617169); 
            tmp1 += z1 + (z2 * FIX_0_071888074); 
            tmp2 += z1 - (z3 * FIX_1_125726048); 
            z1 = ((z3 - z2) * FIX_1_407403738); 
            tmp11 += z1 - (z3 * FIX_0_766367282); 
            tmp12 += z1 + (z2 * FIX_1_971951411); 
            z2 += z4;
            z1 = (z2 * (-FIX_0_666655658)); 
            tmp1 += z1;
            tmp3 += z1 + (z4 * FIX_1_065388962); 
            z2 = (z2 * (-FIX_1_247225013)); 
            tmp10 += z2 + (z4 * FIX_3_141271809); 
            tmp12 += z2;
            z2 = ((z3 + z4) * (-FIX_1_353318001)); 
            tmp2 += z2;
            tmp3 += z2;
            z2 = ((z4 - z3) * FIX_0_410524528); 
            tmp10 += z2;
            tmp11 += z2;

            
            wsptr[wsOffset + 8 * 0] = (tmp20 + tmp0) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 15] = (tmp20 - tmp0) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 1] = (tmp21 + tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 14] = (tmp21 - tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 2] = (tmp22 + tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 13] = (tmp22 - tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 3] = (tmp23 + tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 12] = (tmp23 - tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 4] = (tmp24 + tmp10) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 11] = (tmp24 - tmp10) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 5] = (tmp25 + tmp11) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 10] = (tmp25 - tmp11) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 6] = (tmp26 + tmp12) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 9] = (tmp26 - tmp12) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 7] = (tmp27 + tmp13) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 8] = (tmp27 - tmp13) >> (CONST_BITS - PASS1_BITS);
        }

        

        wsOffset = 0;
        for (ctr = 0; ctr < 16; ctr++) {
            if (ctr < 8) {
                outputOffset1 = outPos + ctr * 8;
                outputOffset2 = outPos + DCTSIZE2 + ctr * 8;
            } else {
                outputOffset1 = outPos + DCTSIZE2 + ctr * 8;
                outputOffset2 = outPos + DCTSIZE2 * 2 + ctr * 8;
            }
            

            
            tmp0 = wsptr[wsOffset] + (ONE << (PASS1_BITS + 2));
            tmp0 <<= CONST_BITS;

            z1 = wsptr[wsOffset + 4];
            tmp1 = (z1 * FIX_1_306562965); 
            tmp2 = (z1 * FIX_0_541196100); 

            tmp10 = tmp0 + tmp1;
            tmp11 = tmp0 - tmp1;
            tmp12 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;

            z1 = wsptr[wsOffset + 2];
            z2 = wsptr[wsOffset + 6];
            z3 = z1 - z2;
            z4 = (z3 * FIX_0_275899379); 
            z3 = (z3 * FIX_1_387039845); 

            // (c6+c2)[16] = (c3+c1)[8]
            tmp0 = z3 + (z2 * FIX_2_562915447);
            // (c6-c14)[16] = (c3-c7)[8]
            tmp1 = z4 + (z1 * FIX_0_899976223);
            // (c2-c10)[16] = (c1-c5)[8]
            tmp2 = z3 - (z1 * FIX_0_601344887);
            // (c10-c14)[16] = (c5-c7)[8]
            tmp3 = z4 - (z2 * FIX_0_509795579);

            tmp20 = tmp10 + tmp0;
            tmp27 = tmp10 - tmp0;
            tmp21 = tmp12 + tmp1;
            tmp26 = tmp12 - tmp1;
            tmp22 = tmp13 + tmp2;
            tmp25 = tmp13 - tmp2;
            tmp23 = tmp11 + tmp3;
            tmp24 = tmp11 - tmp3;

            

            z1 = wsptr[wsOffset + 1];
            z2 = wsptr[wsOffset + 3];
            z3 = wsptr[wsOffset + 5];
            z4 = wsptr[wsOffset + 7];

            tmp11 = z1 + z3;

            tmp1 = ((z1 + z2) * FIX_1_353318001); 
            tmp2 = (tmp11 * FIX_1_247225013); 
            tmp3 = ((z1 + z4) * FIX_1_093201867); 
            tmp10 = ((z1 - z4) * FIX_0_897167586); 
            tmp11 = (tmp11 * FIX_0_666655658); 
            tmp12 = ((z1 - z2) * FIX_0_410524528); 
            tmp0 = tmp1 + tmp2 + tmp3 - (z1 * FIX_2_286341144);
            tmp13 = tmp10 + tmp11 + tmp12 - (z1 * FIX_1_835730603);
            z1 = ((z2 + z3) * FIX_0_138617169); 
            tmp1 += z1 + (z2 * FIX_0_071888074); 
            tmp2 += z1 - (z3 * FIX_1_125726048); 
            z1 = ((z3 - z2) * FIX_1_407403738); 
            tmp11 += z1 - (z3 * FIX_0_766367282); 
            tmp12 += z1 + (z2 * FIX_1_971951411); 
            z2 += z4;
            z1 = (z2 * (-FIX_0_666655658)); 
            tmp1 += z1;
            tmp3 += z1 + (z4 * FIX_1_065388962); 
            z2 = (z2 * (-FIX_1_247225013)); 
            tmp10 += z2 + (z4 * FIX_3_141271809); 
            tmp12 += z2;
            z2 = ((z3 + z4) * (-FIX_1_353318001)); 
            tmp2 += z2;
            tmp3 += z2;
            z2 = ((z4 - z3) * FIX_0_410524528); 
            tmp10 += z2;
            tmp11 += z2;

            
            outptr[outputOffset1 + 0] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp20 + tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 7] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp20 - tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 1] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp21 + tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 6] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp21 - tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 2] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp22 + tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 5] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp22 - tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 3] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp23 + tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 4] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp23 - tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 4] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp24 + tmp10) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 3] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp24 - tmp10) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 5] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp25 + tmp11) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 2] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp25 - tmp11) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 6] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp26 + tmp12) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 1] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp26 - tmp12) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 7] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp27 + tmp13) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 0] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp27 - tmp13) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];

            wsOffset += 8; 
        }

        return outptr;
    }

    protected Object calculate16x8(int[] inptr, int inPos, int[] quant, int[] output, int outPos) {
        int tmp0, tmp1, tmp2, tmp3, tmp10, tmp11, tmp12, tmp13;
        int tmp20, tmp21, tmp22, tmp23, tmp24, tmp25, tmp26, tmp27;
        int z1, z2, z3, z4;
        int[] wsptr;
        int[] outptr = (output != null ? output : new int[DCTSIZE2 * 2]);
        short[] rangeLimitMap = sampleRangeLimitTable;
        int rangeLimitOffset = sampleRangeLimitOffset + CENTERJSAMPLE;

        int ctr;
        int workspace[] = new int[DCTSIZE2]; 
        int inputOffset = inPos, outputOffset1 = 0, outputOffset2 = 0, quantOffset = 0, wsOffset = 0;

        
        
        

        wsptr = workspace;
        for (ctr = DCTSIZE; ctr > 0; ctr--) {
            if (inptr[inputOffset + DCTSIZE * 1] == 0 && inptr[inputOffset + DCTSIZE * 2] == 0
                && inptr[inputOffset + DCTSIZE * 3] == 0 && inptr[inputOffset + DCTSIZE * 4] == 0
                && inptr[inputOffset + DCTSIZE * 5] == 0 && inptr[inputOffset + DCTSIZE * 6] == 0
                && inptr[inputOffset + DCTSIZE * 7] == 0) {
                
                int dcval = (inptr[inputOffset] * quant[quantOffset]) << PASS1_BITS;

                wsptr[wsOffset + DCTSIZE * 0] = dcval;
                wsptr[wsOffset + DCTSIZE * 1] = dcval;
                wsptr[wsOffset + DCTSIZE * 2] = dcval;
                wsptr[wsOffset + DCTSIZE * 3] = dcval;
                wsptr[wsOffset + DCTSIZE * 4] = dcval;
                wsptr[wsOffset + DCTSIZE * 5] = dcval;
                wsptr[wsOffset + DCTSIZE * 6] = dcval;
                wsptr[wsOffset + DCTSIZE * 7] = dcval;

                
                inputOffset++;
                quantOffset++;
                wsOffset++;

                continue;
            }

            
            

            z2 = inptr[inputOffset + DCTSIZE * 2] * quant[quantOffset + DCTSIZE * 2];
            z3 = inptr[inputOffset + DCTSIZE * 6] * quant[quantOffset + DCTSIZE * 6];

            z1 = ((z2 + z3) * FIX_0_541196100);
            tmp2 = z1 + (z2 * FIX_0_765366865);
            tmp3 = z1 - (z3 * FIX_1_847759065);

            z2 = inptr[inputOffset + DCTSIZE * 0] * quant[quantOffset + DCTSIZE * 0];
            z3 = inptr[inputOffset + DCTSIZE * 4] * quant[quantOffset + DCTSIZE * 4];
            z2 <<= CONST_BITS;
            z3 <<= CONST_BITS;
            
            z2 += (ONE << (CONST_BITS - PASS1_BITS - 1));

            tmp0 = z2 + z3;
            tmp1 = z2 - z3;

            tmp10 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;
            tmp11 = tmp1 + tmp3;
            tmp12 = tmp1 - tmp3;

            

            tmp0 = inptr[inputOffset + DCTSIZE * 7] * quant[quantOffset + DCTSIZE * 7];
            tmp1 = inptr[inputOffset + DCTSIZE * 5] * quant[quantOffset + DCTSIZE * 5];
            tmp2 = inptr[inputOffset + DCTSIZE * 3] * quant[quantOffset + DCTSIZE * 3];
            tmp3 = inptr[inputOffset + DCTSIZE * 1] * quant[quantOffset + DCTSIZE * 1];

            z2 = tmp0 + tmp2;
            z3 = tmp1 + tmp3;

            z1 = ((z2 + z3) * FIX_1_175875602); 
            z2 = (z2 * (-FIX_1_961570560)); 
            z3 = (z3 * (-FIX_0_390180644)); 
            z2 += z1;
            z3 += z1;

            z1 = ((tmp0 + tmp3) * (-FIX_0_899976223)); 
            tmp0 = (tmp0 * FIX_0_298631336); 
            tmp3 = (tmp3 * FIX_1_501321110); 
            tmp0 += z1 + z2;
            tmp3 += z1 + z3;

            z1 = ((tmp1 + tmp2) * (-FIX_2_562915447)); 
            tmp1 = (tmp1 * FIX_2_053119869); 
            tmp2 = (tmp2 * FIX_3_072711026); 
            tmp1 += z1 + z3;
            tmp2 += z1 + z2;

            

            wsptr[wsOffset + DCTSIZE * 0] = (tmp10 + tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 7] = (tmp10 - tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 1] = (tmp11 + tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 6] = (tmp11 - tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 2] = (tmp12 + tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 5] = (tmp12 - tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 3] = (tmp13 + tmp0) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 4] = (tmp13 - tmp0) >> (CONST_BITS - PASS1_BITS);

            
            inputOffset++;
            quantOffset++;
            wsOffset++;
        }

        
        wsOffset = 0;
        for (ctr = 0; ctr < DCTSIZE; ctr++) {
            // outptr = output_buf[ctr] + output_col;
            outputOffset1 = outPos + ctr * DCTSIZE;
            outputOffset2 = outPos + DCTSIZE2 + ctr * DCTSIZE;

            

            
            tmp0 = wsptr[wsOffset] + (ONE << (PASS1_BITS + 2));
            tmp0 <<= CONST_BITS;

            z1 = wsptr[wsOffset + 4];
            tmp1 = (z1 * FIX_1_306562965); 
            tmp2 = (z1 * FIX_0_541196100); 

            tmp10 = tmp0 + tmp1;
            tmp11 = tmp0 - tmp1;
            tmp12 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;

            z1 = wsptr[wsOffset + 2];
            z2 = wsptr[wsOffset + 6];
            z3 = z1 - z2;
            z4 = (z3 * FIX_0_275899379); 
            z3 = (z3 * FIX_1_387039845); 

            // (c6+c2)[16] = (c3+c1)[8]
            tmp0 = z3 + (z2 * FIX_2_562915447);
            // (c6-c14)[16] = (c3-c7)[8]
            tmp1 = z4 + (z1 * FIX_0_899976223);
            // (c2-c10)[16] = (c1-c5)[8]
            tmp2 = z3 - (z1 * FIX_0_601344887);
            // (c10-c14)[16] = (c5-c7)[8]
            tmp3 = z4 - (z2 * FIX_0_509795579);

            tmp20 = tmp10 + tmp0;
            tmp27 = tmp10 - tmp0;
            tmp21 = tmp12 + tmp1;
            tmp26 = tmp12 - tmp1;
            tmp22 = tmp13 + tmp2;
            tmp25 = tmp13 - tmp2;
            tmp23 = tmp11 + tmp3;
            tmp24 = tmp11 - tmp3;

            
            z1 = wsptr[wsOffset + 1];
            z2 = wsptr[wsOffset + 3];
            z3 = wsptr[wsOffset + 5];
            z4 = wsptr[wsOffset + 7];

            tmp11 = z1 + z3;

            tmp1 = ((z1 + z2) * FIX_1_353318001); 
            tmp2 = (tmp11 * FIX_1_247225013); 
            tmp3 = ((z1 + z4) * FIX_1_093201867); 
            tmp10 = ((z1 - z4) * FIX_0_897167586); 
            tmp11 = (tmp11 * FIX_0_666655658); 
            tmp12 = ((z1 - z2) * FIX_0_410524528); 
            // c7+c5+c3-c1
            tmp0 = tmp1 + tmp2 + tmp3 - (z1 * FIX_2_286341144);
            tmp13 = tmp10 + tmp11 + tmp12 - (z1 * FIX_1_835730603);
            z1 = ((z2 + z3) * FIX_0_138617169); 
            tmp1 += z1 + (z2 * FIX_0_071888074); 
            tmp2 += z1 - (z3 * FIX_1_125726048); 
            z1 = ((z3 - z2) * FIX_1_407403738); 
            tmp11 += z1 - (z3 * FIX_0_766367282); 
            tmp12 += z1 + (z2 * FIX_1_971951411); 
            z2 += z4;
            z1 = (z2 * (-FIX_0_666655658)); 
            tmp1 += z1;
            tmp3 += z1 + (z4 * FIX_1_065388962); 
            z2 = (z2 * (-FIX_1_247225013)); 
            tmp10 += z2 + (z4 * FIX_3_141271809); 
            tmp12 += z2;
            z2 = ((z3 + z4) * (-FIX_1_353318001)); 
            tmp2 += z2;
            tmp3 += z2;
            z2 = ((z4 - z3) * FIX_0_410524528); 
            tmp10 += z2;
            tmp11 += z2;

            
            outptr[outputOffset1 + 0] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp20 + tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 7] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp20 - tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 1] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp21 + tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 6] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp21 - tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 2] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp22 + tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 5] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp22 - tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 3] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp23 + tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 4] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp23 - tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 4] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp24 + tmp10) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 3] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp24 - tmp10) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 5] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp25 + tmp11) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 2] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp25 - tmp11) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 6] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp26 + tmp12) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 1] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp26 - tmp12) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset1 + 7] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp27 + tmp13) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset2 + 0] = rangeLimitMap[rangeLimitOffset
                                                      + (((tmp27 - tmp13) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];

            wsOffset += 8; 
        }

        return outptr;
    }

    protected Object calculate8x16(int[] inptr, int inPos, int[] quant, int[] output, int outPos) {
        int tmp0, tmp1, tmp2, tmp3, tmp10, tmp11, tmp12, tmp13;
        int tmp20, tmp21, tmp22, tmp23, tmp24, tmp25, tmp26, tmp27;
        int z1, z2, z3, z4;
        int[] wsptr;
        int[] outptr = (output != null ? output : new int[DCTSIZE2 * 2]);

        short[] rangeLimitMap = sampleRangeLimitTable;
        int rangeLimitOffset = sampleRangeLimitOffset + CENTERJSAMPLE;

        int ctr;
        int[] workspace = new int[8 * 16]; 

        int inputOffset = inPos, quantOffset = 0, outputOffset = 0, wsOffset = 0;
        
        wsptr = workspace;

        for (ctr = 0; ctr < 8; ctr++, inputOffset++, quantOffset++, wsOffset++) {
            

            tmp0 = inptr[inputOffset] * quant[quantOffset];
            tmp0 <<= CONST_BITS;
            
            tmp0 += (ONE << (CONST_BITS - PASS1_BITS - 1));

            z1 = inptr[inputOffset + DCTSIZE * 4] * quant[quantOffset + DCTSIZE * 4];
            tmp1 = (z1 * FIX_1_306562965); 
            tmp2 = (z1 * FIX_0_541196100); 

            tmp10 = tmp0 + tmp1;
            tmp11 = tmp0 - tmp1;
            tmp12 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;

            z1 = inptr[inputOffset + DCTSIZE * 2] * quant[quantOffset + DCTSIZE * 2];
            z2 = inptr[inputOffset + DCTSIZE * 6] * quant[quantOffset + DCTSIZE * 6];
            z3 = z1 - z2;
            z4 = (z3 * FIX_0_275899379); 
            z3 = (z3 * FIX_1_387039845); 

            // (c6+c2)[16] = (c3+c1)[8]
            tmp0 = z3 + (z2 * FIX_2_562915447);
            // (c6-c14)[16] = (c3-c7)[8]
            tmp1 = z4 + (z1 * FIX_0_899976223);
            // (c2-c10)[16] = (c1-c5)[8]
            tmp2 = z3 - (z1 * FIX_0_601344887);
            // (c10-c14)[16] = (c5-c7)[8]
            tmp3 = z4 - (z2 * FIX_0_509795579);

            tmp20 = tmp10 + tmp0;
            tmp27 = tmp10 - tmp0;
            tmp21 = tmp12 + tmp1;
            tmp26 = tmp12 - tmp1;
            tmp22 = tmp13 + tmp2;
            tmp25 = tmp13 - tmp2;
            tmp23 = tmp11 + tmp3;
            tmp24 = tmp11 - tmp3;

            

            z1 = inptr[inputOffset + DCTSIZE * 1] * quant[quantOffset + DCTSIZE * 1];
            z2 = inptr[inputOffset + DCTSIZE * 3] * quant[quantOffset + DCTSIZE * 3];
            z3 = inptr[inputOffset + DCTSIZE * 5] * quant[quantOffset + DCTSIZE * 5];
            z4 = inptr[inputOffset + DCTSIZE * 7] * quant[quantOffset + DCTSIZE * 7];

            tmp11 = z1 + z3;

            tmp1 = ((z1 + z2) * FIX_1_353318001); 
            tmp2 = (tmp11 * FIX_1_247225013); 
            tmp3 = ((z1 + z4) * FIX_1_093201867); 
            tmp10 = ((z1 - z4) * FIX_0_897167586); 
            tmp11 = (tmp11 * FIX_0_666655658); 
            tmp12 = ((z1 - z2) * FIX_0_410524528); 
            // c7+c5+c3-c1
            tmp0 = tmp1 + tmp2 + tmp3 - (z1 * FIX_2_286341144);
            tmp13 = tmp10 + tmp11 + tmp12 - (z1 * FIX_1_835730603);
            z1 = ((z2 + z3) * FIX_0_138617169); 
            tmp1 += z1 + (z2 * FIX_0_071888074); 
            tmp2 += z1 - (z3 * FIX_1_125726048); 
            z1 = ((z3 - z2) * FIX_1_407403738); 
            tmp11 += z1 - (z3 * FIX_0_766367282); 
            tmp12 += z1 + (z2 * FIX_1_971951411); 
            z2 += z4;
            z1 = (z2 * (-FIX_0_666655658)); 
            tmp1 += z1;
            tmp3 += z1 + (z4 * FIX_1_065388962); 
            z2 = (z2 * (-FIX_1_247225013)); 
            tmp10 += z2 + (z4 * FIX_3_141271809); 
            tmp12 += z2;
            z2 = ((z3 + z4) * (-FIX_1_353318001)); 
            tmp2 += z2;
            tmp3 += z2;
            z2 = ((z4 - z3) * FIX_0_410524528); 
            tmp10 += z2;
            tmp11 += z2;

            

            wsptr[wsOffset + 8 * 0] = (tmp20 + tmp0) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 15] = (tmp20 - tmp0) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 1] = (tmp21 + tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 14] = (tmp21 - tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 2] = (tmp22 + tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 13] = (tmp22 - tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 3] = (tmp23 + tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 12] = (tmp23 - tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 4] = (tmp24 + tmp10) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 11] = (tmp24 - tmp10) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 5] = (tmp25 + tmp11) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 10] = (tmp25 - tmp11) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 6] = (tmp26 + tmp12) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 9] = (tmp26 - tmp12) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 7] = (tmp27 + tmp13) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + 8 * 8] = (tmp27 - tmp13) >> (CONST_BITS - PASS1_BITS);
        }

        
        
        

        // wsptr = workspace;
        wsOffset = 0;
        for (ctr = 0; ctr < 16; ctr++) {
            outputOffset = outPos + ctr * 8;

            
            

            z2 = wsptr[wsOffset + 2];
            z3 = wsptr[wsOffset + 6];

            z1 = ((z2 + z3) * FIX_0_541196100);
            tmp2 = z1 + (z2 * FIX_0_765366865);
            tmp3 = z1 - (z3 * FIX_1_847759065);

            
            z2 = wsptr[wsOffset] + (ONE << (PASS1_BITS + 2));
            z3 = wsptr[wsOffset + 4];

            tmp0 = (z2 + z3) << CONST_BITS;
            tmp1 = (z2 - z3) << CONST_BITS;

            tmp10 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;
            tmp11 = tmp1 + tmp3;
            tmp12 = tmp1 - tmp3;

            

            tmp0 = wsptr[wsOffset + 7];
            tmp1 = wsptr[wsOffset + 5];
            tmp2 = wsptr[wsOffset + 3];
            tmp3 = wsptr[wsOffset + 1];

            z2 = tmp0 + tmp2;
            z3 = tmp1 + tmp3;

            z1 = ((z2 + z3) * FIX_1_175875602); 
            z2 = (z2 * (-FIX_1_961570560)); 
            z3 = (z3 * (-FIX_0_390180644)); 
            z2 += z1;
            z3 += z1;

            z1 = ((tmp0 + tmp3) * (-FIX_0_899976223)); 
            tmp0 = (tmp0 * FIX_0_298631336); 
            tmp3 = (tmp3 * FIX_1_501321110); 
            tmp0 += z1 + z2;
            tmp3 += z1 + z3;

            z1 = ((tmp1 + tmp2) * (-FIX_2_562915447)); 
            tmp1 = (tmp1 * FIX_2_053119869); 
            tmp2 = (tmp2 * FIX_3_072711026); 
            tmp1 += z1 + z3;
            tmp2 += z1 + z2;

            
            outptr[outputOffset + 0] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp10 + tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 7] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp10 - tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 1] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp11 + tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 6] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp11 - tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 2] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp12 + tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 5] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp12 - tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 3] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp13 + tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 4] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp13 - tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];

            wsOffset += DCTSIZE; 
        }

        return outptr;
    }

    protected Object calculate8x8(int[] inptr, int inPos, int[] quant, int[] output, int outPos) {
        int tmp0, tmp1, tmp2, tmp3;
        int tmp10, tmp11, tmp12, tmp13;
        int z1, z2, z3;
        int[] wsptr;
        int[] outptr = (output != null ? output : new int[DCTSIZE2]);

        short[] rangeLimitMap = sampleRangeLimitTable;
        int rangeLimitOffset = sampleRangeLimitOffset + CENTERJSAMPLE;

        int ctr;
        int workspace[] = new int[DCTSIZE2]; 

        int inputOffset = inPos, wsOffset = 0, quantOffset = 0, outputOffset = 0;
        
        
        
        wsptr = workspace;

        for (ctr = DCTSIZE; ctr > 0; ctr--) {
            if (inptr[inputOffset + DCTSIZE * 1] == 0 && inptr[inputOffset + DCTSIZE * 2] == 0
                && inptr[inputOffset + DCTSIZE * 3] == 0 && inptr[inputOffset + DCTSIZE * 4] == 0
                && inptr[inputOffset + DCTSIZE * 5] == 0 && inptr[inputOffset + DCTSIZE * 6] == 0
                && inptr[inputOffset + DCTSIZE * 7] == 0) {
                
                int dcval = (inptr[inputOffset] * quant[quantOffset]) << PASS1_BITS;

                wsptr[wsOffset + DCTSIZE * 0] = dcval;
                wsptr[wsOffset + DCTSIZE * 1] = dcval;
                wsptr[wsOffset + DCTSIZE * 2] = dcval;
                wsptr[wsOffset + DCTSIZE * 3] = dcval;
                wsptr[wsOffset + DCTSIZE * 4] = dcval;
                wsptr[wsOffset + DCTSIZE * 5] = dcval;
                wsptr[wsOffset + DCTSIZE * 6] = dcval;
                wsptr[wsOffset + DCTSIZE * 7] = dcval;

                
                inputOffset++;
                quantOffset++;
                wsOffset++;

                continue;
            }

            
            

            z2 = inptr[inputOffset + DCTSIZE * 2] * quant[quantOffset + DCTSIZE * 2];
            z3 = inptr[inputOffset + DCTSIZE * 6] * quant[quantOffset + DCTSIZE * 6];

            z1 = ((z2 + z3) * FIX_0_541196100);
            tmp2 = z1 + (z2 * FIX_0_765366865);
            tmp3 = z1 - (z3 * FIX_1_847759065);

            z2 = inptr[inputOffset + DCTSIZE * 0] * quant[quantOffset + DCTSIZE * 0];
            z3 = inptr[inputOffset + DCTSIZE * 4] * quant[quantOffset + DCTSIZE * 4];
            z2 <<= CONST_BITS;
            z3 <<= CONST_BITS;
            
            z2 += (ONE << (CONST_BITS - PASS1_BITS - 1));

            tmp0 = z2 + z3;
            tmp1 = z2 - z3;

            tmp10 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;
            tmp11 = tmp1 + tmp3;
            tmp12 = tmp1 - tmp3;

            

            tmp0 = inptr[inputOffset + DCTSIZE * 7] * quant[quantOffset + DCTSIZE * 7];
            tmp1 = inptr[inputOffset + DCTSIZE * 5] * quant[quantOffset + DCTSIZE * 5];
            tmp2 = inptr[inputOffset + DCTSIZE * 3] * quant[quantOffset + DCTSIZE * 3];
            tmp3 = inptr[inputOffset + DCTSIZE * 1] * quant[quantOffset + DCTSIZE * 1];

            z2 = tmp0 + tmp2;
            z3 = tmp1 + tmp3;

            z1 = ((z2 + z3) * FIX_1_175875602); 
            z2 = (z2 * (-FIX_1_961570560)); 
            z3 = (z3 * (-FIX_0_390180644)); 
            z2 += z1;
            z3 += z1;

            z1 = ((tmp0 + tmp3) * (-FIX_0_899976223)); 
            tmp0 = (tmp0 * FIX_0_298631336); 
            tmp3 = (tmp3 * FIX_1_501321110); 
            tmp0 += z1 + z2;
            tmp3 += z1 + z3;

            z1 = ((tmp1 + tmp2) * (-FIX_2_562915447)); 
            tmp1 = (tmp1 * FIX_2_053119869); 
            tmp2 = (tmp2 * FIX_3_072711026); 
            tmp1 += z1 + z3;
            tmp2 += z1 + z2;

            

            wsptr[wsOffset + DCTSIZE * 0] = (tmp10 + tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 7] = (tmp10 - tmp3) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 1] = (tmp11 + tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 6] = (tmp11 - tmp2) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 2] = (tmp12 + tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 5] = (tmp12 - tmp1) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 3] = (tmp13 + tmp0) >> (CONST_BITS - PASS1_BITS);
            wsptr[wsOffset + DCTSIZE * 4] = (tmp13 - tmp0) >> (CONST_BITS - PASS1_BITS);

            
            inputOffset++;
            quantOffset++;
            wsOffset++;
        }

        
        
        

        // wsptr = workspace;
        wsOffset = 0;

        for (ctr = 0; ctr < DCTSIZE; ctr++) {
            outputOffset = outPos + ctr * DCTSIZE;

            if (wsptr[wsOffset + 1] == 0 && wsptr[wsOffset + 2] == 0 && wsptr[wsOffset + 3] == 0
                && wsptr[wsOffset + 4] == 0 && wsptr[wsOffset + 5] == 0 && wsptr[wsOffset + 6] == 0
                && wsptr[wsOffset + 7] == 0) {
                
                int dcval = rangeLimitMap[rangeLimitOffset
                                          + (DESCALE(wsptr[wsOffset + 0], PASS1_BITS + 3) & RANGE_MASK)];

                outptr[outputOffset + 0] = dcval;
                outptr[outputOffset + 1] = dcval;
                outptr[outputOffset + 2] = dcval;
                outptr[outputOffset + 3] = dcval;
                outptr[outputOffset + 4] = dcval;
                outptr[outputOffset + 5] = dcval;
                outptr[outputOffset + 6] = dcval;
                outptr[outputOffset + 7] = dcval;

                wsOffset += DCTSIZE; 

                continue;
            }

            
            

            z2 = wsptr[wsOffset + 2];
            z3 = wsptr[wsOffset + 6];

            z1 = ((z2 + z3) * FIX_0_541196100);
            tmp2 = z1 + (z2 * FIX_0_765366865);
            tmp3 = z1 - (z3 * FIX_1_847759065);

            
            z2 = wsptr[wsOffset] + (ONE << (PASS1_BITS + 2));
            z3 = wsptr[wsOffset + 4];

            tmp0 = (z2 + z3) << CONST_BITS;
            tmp1 = (z2 - z3) << CONST_BITS;

            tmp10 = tmp0 + tmp2;
            tmp13 = tmp0 - tmp2;
            tmp11 = tmp1 + tmp3;
            tmp12 = tmp1 - tmp3;

            

            tmp0 = wsptr[wsOffset + 7];
            tmp1 = wsptr[wsOffset + 5];
            tmp2 = wsptr[wsOffset + 3];
            tmp3 = wsptr[wsOffset + 1];

            z2 = tmp0 + tmp2;
            z3 = tmp1 + tmp3;

            z1 = ((z2 + z3) * FIX_1_175875602); 
            z2 = (z2 * (-FIX_1_961570560)); 
            z3 = (z3 * (-FIX_0_390180644)); 
            z2 += z1;
            z3 += z1;

            z1 = ((tmp0 + tmp3) * (-FIX_0_899976223)); 
            tmp0 = (tmp0 * FIX_0_298631336); 
            tmp3 = (tmp3 * FIX_1_501321110); 
            tmp0 += z1 + z2;
            tmp3 += z1 + z3;

            z1 = ((tmp1 + tmp2) * (-FIX_2_562915447)); 
            tmp1 = (tmp1 * FIX_2_053119869); 
            tmp2 = (tmp2 * FIX_3_072711026); 
            tmp1 += z1 + z3;
            tmp2 += z1 + z2;

            

            outptr[outputOffset + 0] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp10 + tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 7] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp10 - tmp3) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 1] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp11 + tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 6] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp11 - tmp2) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 2] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp12 + tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 5] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp12 - tmp1) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 3] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp13 + tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];
            outptr[outputOffset + 4] = rangeLimitMap[rangeLimitOffset
                                                     + (((tmp13 - tmp0) >> (CONST_BITS + PASS1_BITS + 3)) & RANGE_MASK)];

            wsOffset += DCTSIZE; 
        }

        return outptr;
    }
}
