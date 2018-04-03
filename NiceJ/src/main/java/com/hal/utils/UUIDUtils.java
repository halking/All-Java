package com.hal.utils;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author usubelli
 */
public final class UUIDUtils {
    
    private UUIDUtils() {
        super();
    }

    public static Long generate() {
        long nValue = new BigInteger(34, new HighQualityRandom()).longValue();
        String idString = String.valueOf(nValue); 

        if (idString.length() > 12) {
            idString = idString.substring(0, 12);
        }
        
        return Long.valueOf(idString);
    }
    
    /**
     * @author usubelli
     */
    private static class HighQualityRandom extends Random {

        private static final long serialVersionUID = 3999451117824951248L;
        
        // -------------------------------------------------------------------------
        
        private Lock l  = new ReentrantLock();
        private long u  = 0L;
        private long v = 4101842887655102017L;
        private long w = 1L;
        
        // -------------------------------------------------------------------------
        
        /**
         * default ctor
         */
        public HighQualityRandom() {
            this(System.nanoTime());
        }
        
        // -------------------------------------------------------------------------
        
        /**
         * ctor with a seed parameter
         * @param seed
         */
        public HighQualityRandom(long seed) {
            
            l.lock();
            
            u = seed ^ v;
            nextLong();
            
            v = u;
            nextLong();
            
            w = v;
            nextLong();
            
            l.unlock();
        }
        
        // -------------------------------------------------------------------------
        
        /* Returns the next pseudorandom, uniformly distributed <code>long</code>
         * @see java.util.Random#nextLong()
         */
        public long nextLong() {

            try {
                l.lock();
                
                u = u * 2862933555777941757L + 7046029254386353087L;
                v ^= v >>> 17;
                v ^= v << 31;
                v ^= v >>> 8;
                w = 4294957665L * (w & 0xffffffff) + (w >>> 32);
                
                long x = u ^ (u << 21);
                x ^= x >>> 35;
                x ^= x << 4;
                
                long ret = (x + v) ^ w;
                
                return ret;
                
            } finally {
                l.unlock();
            }
        }
        
        // -------------------------------------------------------------------------
        
        /* 
         * Generates the next pseudorandom number
         * @param   bits random bits
         * @see java.util.Random#next(int)
         */
        protected int next(int bits) {
            return (int) (nextLong() >>> (64 - bits));
        }
    }

}


