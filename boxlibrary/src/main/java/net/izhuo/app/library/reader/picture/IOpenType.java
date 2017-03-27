/**
 * Copyright © All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.reader.picture;

/**
 * @author Changlei
 *         <p>
 *         2015年2月15日
 */
public class IOpenType {

    public static final String OPEN_TYPE = "IOpenType";

    public enum Type {
        // 利用构造函数传参
        EXAMINE(1), EDIT(2);

        // 定义私有变量
        private int nCode;

        // 构造函数，枚举类型只能为私有
        Type(int _nCode) {
            this.nCode = _nCode;
        }

        @Override
        public String toString() {
            return String.valueOf(this.nCode);
        }

        public int toInteger() {
            return this.nCode;
        }
    }

}
