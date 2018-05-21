package net.easyappsec.bee2j;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.nio.ByteBuffer;

public interface Bee2CryptoLibrary extends Library {
    Bee2CryptoLibrary INSTANCE = (Bee2CryptoLibrary) Native.loadLibrary(
            Platform.isWindows() ? "bee2" : "libbee2", Bee2CryptoLibrary.class
    );

    interface IRngFunction extends Callback {
        void invoke(PointerByReference buf, int count, PointerByReference stack);
    }

    //тестовая функция brng. theta -- всегда одинаково
    class TestBrngFunc implements IRngFunction {

        public void invoke(PointerByReference buf, int count, PointerByReference state) {

            Bee2CryptoLibrary bee2 = Bee2CryptoLibrary.INSTANCE;

            Pointer p = bee2.beltH();
            byte[] theta = p.getByteArray(128, 32);
            byte[] iv = state.getValue().getByteArray(0, 32);
            byte[] res = buf.getValue().getByteArray(0, count);
            bee2.brngCTRRand(res, count, theta, iv);
            buf.getValue().write(0, res, 0, count);
        }
    }

    class TestBrngForPK implements IRngFunction {

        public void invoke(PointerByReference buf, int count, PointerByReference state) {

            Bee2CryptoLibrary bee2 = Bee2CryptoLibrary.INSTANCE;

            Pointer p = bee2.beltH();
            byte[] theta = p.getByteArray(128, 32);
            byte[] iv = p.getByteArray(192, 32);
            byte[] res = p.getByteArray(0, 96);
            bee2.brngCTRRand(res, count, theta, iv);
            buf.getPointer().write(0, res, 0, count);
        }
    }

    class BrngFuncForPK implements IRngFunction {

        public void invoke(PointerByReference buf, int count, PointerByReference state) {

            Bee2CryptoLibrary bee2 = Bee2CryptoLibrary.INSTANCE;

            ByteBuffer buffer = ByteBuffer.allocate(count);
            buffer.putLong(System.currentTimeMillis());
            byte[] theta = buffer.array();
            byte[] iv = state.getPointer().getByteArray(0, count);
            byte[] res = buf.getPointer().getByteArray(0, count);
            bee2.brngCTRRand(res, count, theta, iv);
            buf.getPointer().write(0, res, 0, count);
        }
    }

    class BrngFunc implements IRngFunction {

        public void invoke(PointerByReference buf, int count, PointerByReference state) {

            Bee2CryptoLibrary bee2 = Bee2CryptoLibrary.INSTANCE;

            ByteBuffer buffer = ByteBuffer.allocate(count);
            buffer.putLong(System.currentTimeMillis());
            byte[] theta = buffer.array();
            byte[] iv = state.getPointer().getByteArray(0, count);
            byte[] res = buf.getPointer().getByteArray(0, count);
            bee2.brngCTRRand(res, count, theta, iv);
            buf.getPointer().write(0, res, 0, count);
        }
    }

    //нативные функции
    Pointer beltH();

    int bignStdParams(BignParams bignParams, String name);

    int bignValParams(BignParams bignParams);

    int bignValPubkey(BignParams bignParams, byte[] pubKey);

    int bignGenKeypair(byte[] privKey, byte[] pubKey, BignParams bignParams,
                       IRngFunction rng, byte[] rng_state);

    int beltECBEncr(byte[] dest, byte[] src, int count,
                    byte[] theta, int len);

    int beltECBDecr(byte[] dest, byte[] src, int count,
                    byte[] theta, int len);

    int beltHash(byte[] hash, byte[] src, int count);

    int bashHash(byte[] hash, int l, byte[] src, int count);

    int bignOidToDER(byte[] oid_der, IntByReference oid_len, String oid);

    int bignSign(
            byte[] sig,                    /*!< [out] подпись */
            BignParams params,    /*!< [in] долговременные параметры */
            byte[] oid_der,            /*!< [in] идентификатор хэш-алгоритма */
            int oid_len,
            byte[] hash,            /*!< [in] хэш-значение */
            byte[] privkey,        /*!< [in] личный ключ */
            IRngFunction rng,                    /*!< [in] генератор случайных чисел */
            byte[] rng_state);

    int bignVerify(
            BignParams params,    /*!< [in] долговременные параметры */
            byte[] oid_der,            /*!< [in] идентификатор хэш-алгоритма */
            int oid_len,
            byte[] hash,
            byte[] sig,            /*!< [in] подпись */
            byte[] pubkey            /*!< [in] открытый ключ */
    );

    int bignCalcPubkey(
            byte[] pubkey,                /*!< [out] открытый ключ */
            BignParams params,    /*!< [in] долговременные параметры */
            byte[] privkey        /*!< [in] личный ключ */
    );

    int bignKeyWrap(
            byte[] token,                    /*!< [out] токен ключа */
            BignParams params,        /*!< [in] долговременные параметры */
            byte[] key,                /*!< [in] транспортируемый ключ */
            int len,                        /*!< [in] длина ключа в октетах */
            byte[] header,            /*!< [in] заголовок ключа [16]*/
            byte[] pubkey,            /*!< [in] открытый ключ получателя */
            IRngFunction rng,                        /*!< [in] генератор случайных чисел */
            Pointer rng_state                    /*!< [in/out] состояние генератора */
    );

    int bignKeyUnwrap(
            byte[] key,                        /*!< [out] ключ */
            BignParams params,        /*!< [in] долговременные параметры */
            byte[] token,                /*!< [in] токен ключа */
            int len,                        /*!< [in] длина токена в октетах */
            byte[] header,            /*!< [in] заголовок ключа [16]*/
            byte[] privkey);            /*!< [in] личный ключ получателя */

    //Модуль brng
    int brngCTR_keep();

    void brngCTRStart(byte[] state, byte[] theta, byte[] iv);

    void brngCTRStepR(byte[] buf, int count, byte[] state);

    void brngCTRStepG(byte[] iv, byte[] state);

    int brngCTRRand(byte[] res, int count, byte[] theta, byte[] iv);

    int beltMAC(byte[] mac, byte[] src, int count, byte[] theta, int len);
}
