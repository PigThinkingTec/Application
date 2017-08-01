package com.pigthinkingtec.framework.dbaccess;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Blob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pigthinkingtec.framework.exception.DatabaseException;
import com.pigthinkingtec.framework.exception.DatabaseExceptionFactory;


/**
 * SQLの結果を保持するクラス
 * 
 * @author yizhou
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class QueryResult {

	/** ログオブジェクト */
	private Log log = LogFactory.getLog(this.getClass().getName());
	
	private List result = null;

	private LinkedMap colName = null;
	
	private int colCount = 0;
	
	private int colIndex = 0;
	

	/**
	 * コンストラクタ
	 * 
	 * @param columnCount
	 */
	public QueryResult(int columnCount) {
		result = new ArrayList();
		colName = new LinkedMap();
		colCount = columnCount;
		colIndex = 0;
	}

	/**
	 * カラム数の取得
	 * 
	 * @return カラム数
	 */
	public int getColumnCount() {
		return colName.size();
	}

	/**
	 * カラム名の取得
	 * 
	 * @return カラム名
	 */
	public List getColumnNames() {
		return colName.asList();
	}

	/**
	 * 結果の追加
	 * 
	 * @param o
	 * @return boolean
	 */
	public boolean add(Object o) {
		return result.add(o);
	}

	/**
	 * カラム名の追加
	 * 
	 * @param columnName
	 */
	public void addColumnName(String columnName) {
		if (!colName.containsKey(columnName)){
			colName.put(columnName, new Integer(colIndex));
		}
		colIndex++;
	}
	
	/**
	 * 行数の取得
	 * 
	 * @return 行数
	 */
	public int getRowCount() {
		if(colCount == 0)
			return (0);
		else 
			return result.size() / colCount;
	}

	/**
	 * Object型のデータ取得
	 * 
	 * @param row　行番号
	 * @param col 列番号
	 * @return Object
	 */
	public Object getObject(int row, int col) {
		if (col < 0 ) {
			return null;
		} else {
			return result.get((row * colCount) + col);
		}
	}

	/**
	 * BigDecimal型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return BigDecimalオブジェクト
	 */
	public BigDecimal getBigDecimal(int row, int col) {
		return (BigDecimal)getObject(row, col);
	}
	
	/**
	 * BigDecimal型のデータ取得
	 * 
	 * @param row 行番号
	 * @param column 列名
	 * @return BigDecimalオブジェクト
	 */
	public BigDecimal getBigDecimal(int row, String column) {
		return (BigDecimal)getObject(row, getColumnIndex(column));
	}
	
	/**
	 * int型のデータ取得
	 * 
	 * @param row　行番号
	 * @param columnName 列名
	 * @return int
	 */
	public int getInt(int row, String columnName) {
		return getInt(row, getColumnIndex(columnName));
	}

	/**
	 * int型のデータ取得
	 * 
	 * @param row　行番号
	 * @param col 列番号
	 * @return int
	 */
	public int getInt(int row, int col) {
		if (isNull(row, col)) {
			return (0);
		}

		Object o = getObject(row, col);
		if (o instanceof Integer) {
			return (((Integer) o).intValue());
		} else {
			return (((BigDecimal) o).intValue());
		}
	}

	/**
	 * Integer型のデータ取得
	 * 
	 * @param row　行番号
	 * @param columnName 列名
	 * @return Integer
	 */
	public Integer getIntegerObject(int row, String columnName) {
		return getIntegerObject(row, getColumnIndex(columnName));
	}

	/**
	 * Integer型のデータ取得
	 * 
	 * @param row　行番号
	 * @param col 列番号
	 * @return Integer
	 */
	public Integer getIntegerObject(int row, int col) {
		if (isNull(row, col)) {
			return null;
		}

		Object o = getObject(row, col);
		if (o instanceof Integer) {
			return ((Integer) o);
		} else {
			return new Integer(((BigDecimal) o).intValue());
		}
	}
	
	/**
	 * long型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return long
	 */
	public long getLong(int row, String columnName) {
		return (getLong(row, getColumnIndex(columnName)));
	}

	/**
	 * long型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return long
	 */
	public long getLong(int row, int col) {
		if (isNull(row, col))
			return (0);

		Object o = getObject(row, col);
		if (o instanceof Long)
			return (((Long) o).longValue());
		else if (o instanceof Integer)
			return (((Integer) o).longValue());
		else
			return (((BigDecimal) o).longValue());
	}

	/**
	 * Long型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return Long
	 */
	public Long getLongObject(int row, String columnName) {
		return (getLongObject(row, getColumnIndex(columnName)));
	}

	/**
	 * Long型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return Long
	 */
	public Long getLongObject(int row, int col) {
		if (isNull(row, col))
			return null;

		Object o = getObject(row, col);
		if (o instanceof Long)
			return ((Long) o);
		else if (o instanceof Integer)
			return new Long(((Integer) o).longValue());
		else
			return new Long(((BigDecimal) o).longValue());
	}
	
	/**
	 * String型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return String
	 */
	public String getString(int row, int col) {
		Object o = getObject(row, col);
		if (o == null) {
			return null;
		} else if (o instanceof BigDecimal) {
			return ("" + getBigDecimal(row, col).toPlainString());
        }else if (o instanceof Date){
            return new SimpleDateFormat("yyyyMMdd").format(getDate(row, col));
	    }else if (o instanceof Timestamp) {
	            return new SimpleDateFormat("yyyyMMdd HHmmss").format(getDate(row, col));
	    } else if (o instanceof byte[]) {
	    	return Arrays.toString((byte[])o);
	    }else {
			String s = o.toString();
			return (s);
		}
	}
	
	/**
	 * String型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return String
	 */
	public String getString(int row, String columnName) {
		int i = getColumnIndex(columnName);
		if (i < 0) {
			log.debug("column not found:" + columnName);
			return null;
		} else {
			return (getString(row, i));
		}
	}

	/**
	 * boolean型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return boolean
	 */
	public boolean getBoolean(int row, int col) {
		if (isNull(row, col))
			return (false);

		Object o = getObject(row, col);
		Boolean b = (Boolean) o;
		return (b.booleanValue());
	}
	
	/**
	 * boolean型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return boolean
	 */
	public boolean getBoolean(int row, String columnName) {
		return (getBoolean(row, getColumnIndex(columnName)));
	}

	/**
	 * Date型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return Date
	 */
	public Date getDate(int row, int col) {
		if (isNull(row, col))
			return (null);

		Object o = getObject(row, col);
		if (o instanceof Timestamp) {
			Timestamp t = (Timestamp) o;
			Date d = new Date(t.getTime());
			return (d);
		} else {
			Date d = (Date) o;
			return (d);
		}
	}

	/**
	 * Date型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return Date
	 */
	public Date getDate(int row, String columnName) {
		return (getDate(row, getColumnIndex(columnName)));
	}

	/**
	 * Time型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return Time
	 */
	public Time getTime(int row, int col) {
		if (isNull(row, col))
			return (null);

		Object o = getObject(row, col);
		Time t = (Time) o;
		return (t);
	}

	/**
	 * Time型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return Time
	 */
	public Time getTime(int row, String columnName) {
		return (getTime(row, getColumnIndex(columnName)));
	}

	/**
	 * Timestamp型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return Timestamp
	 */
	public Timestamp getTimestamp(int row, int col) {
		if (isNull(row, col))
			return (null);

		Object o = getObject(row, col);
		if (o instanceof Date) {
			Date d = (Date) o;
			Timestamp t = new Timestamp(d.getTime());
			return (t);
		} else {
			Timestamp t = (Timestamp) o;
			return (t);
		}
	}

	/**
	 * Timestamp型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return Timestamp
	 */
	public Timestamp getTimestamp(int row, String columnName) {
		return (getTimestamp(row, getColumnIndex(columnName)));
	}

	/**
	 * double型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return double
	 */
	public double getDouble(int row, String columnName) {
		return (getDouble(row, getColumnIndex(columnName)));
	}
	
	/**
	 * double型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return double
	 */
	public double getDouble(int row, int col) {
		if (isNull(row, col))
			return (0);

		Object o = getObject(row, col);
		if (o instanceof BigDecimal) {
			BigDecimal b = (BigDecimal) o;
			return (b.doubleValue());
		} else {
			Double d = (Double) o;
			return (d.doubleValue());
		}
	}
	
	/**
	 * Double型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return Double
	 */
	public Double getDoubleObject(int row, String columnName) {
		return (getDoubleObject(row, getColumnIndex(columnName)));
	}
	
	/**
	 * Double型のオブジェクトを取得する
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return Double
	 */
	public Double getDoubleObject(int row, int col) {
		if (isNull(row, col))
			return null;

		Object o = getObject(row, col);
		if (o instanceof BigDecimal) {
			return new Double(((BigDecimal) o).doubleValue());
		} else {
			return ((Double) o);
		}
	}

	/**
	 * float型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return float
	 */
	public float getFloat(int row, String columnName) {
		return (getFloat(row, getColumnIndex(columnName)));
	}
	
	/**
	 * float型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return float
	 */
	public float getFloat(int row, int col) {
		if (isNull(row, col))
			return (0);

		Object o = getObject(row, col);
		if (o instanceof BigDecimal) {
			BigDecimal b = (BigDecimal) o;
			return (b.floatValue());
		} else {
			Float f = (Float) o;
			return (f.floatValue());
		}
	}

	/**
	 * Float型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return Float
	 */
	public Float getFloatObject(int row, String columnName) {
		return (getFloatObject(row, getColumnIndex(columnName)));
	}
	
	/**
	 * Float型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return Float
	 */
	public Float getFloatObject(int row, int col) {
		if (isNull(row, col))
			return null;

		Object o = getObject(row, col);
		if (o instanceof BigDecimal) {
			return new Float(((BigDecimal) o).floatValue());
		} else {
			return ((Float) o);
		}
	}

	/**
	 * String型の整数取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return String
	 */
	public String getIntegerString(int row, String columnName) {
		return (getIntegerString(row, getColumnIndex(columnName)));
	}

	/**
	 * String型の整数取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return String
	 */
	public String getIntegerString(int row, int col) {
		if (isNull(row, col)) {
			return null;
		}
		
		Object o = getObject(row, col);
		if (o instanceof BigDecimal) {
			return String.valueOf(new Long(((BigDecimal) o).longValue()));
		} else {
			return (String.valueOf(o));
		}
	}

        /**
         * 
         * @param row
         * @param columnName
         * @return
         * @throws DatabaseException 
         */
        public Blob getBlob(int row, String columnName) throws DatabaseException {
            return (getBlob(row, getColumnIndex(columnName)));
        }
        
        /**
         * 
         * @param row
         * @param col
         * @return
         * @throws DatabaseException 
         */
        public Blob getBlob(int row, int col) throws DatabaseException {
            if (isNull(row, col)) {
		return null;
            }
		
            Object o = getObject(row, col);
            
            return (Blob)o;
        }
        
        /**
         * 
         * @param row
         * @param columnName
         * @return
         * @throws DatabaseException 
         */
        public InputStream getInputStream(int row, String columnName) throws DatabaseException {
            try {
            	ByteArrayOutputStream os = new ByteArrayOutputStream();
                InputStream is = getBlob(row, columnName).getBinaryStream();
                byte[] b = new byte[256];
                int length = 0;
                while((length = is.read(b)) > 0) {
                	os.write(b, 0, length);
                };
                is.close();
                InputStream returnIs = new ByteArrayInputStream(os.toByteArray());
                os.close();
                return returnIs;
                
            } catch (SQLException ex) {
                throw DatabaseExceptionFactory.createException(ex);
            } catch (IOException e) {
				throw new DatabaseException(e);
			}
        }
        
        /**
         * 
         * @param row
         * @param col
         * @return
         * @throws DatabaseException 
         */
        public InputStream getInputStream(int row, int col) throws DatabaseException {
            try {
                InputStream is = getBlob(row, col).getBinaryStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] b = new byte[256];
                int length = 0;
                while((length = is.read(b)) > 0) {
                	os.write(b, 0, length);
                };
                
                is.close();
                InputStream returnIs = new ByteArrayInputStream(os.toByteArray());
                os.close();
                return returnIs;
            } catch (SQLException ex) {
                throw DatabaseExceptionFactory.createException(ex);
            } catch (IOException e) {
            	throw new DatabaseException(e);
			}
        }
        

	/*******************************************************************/

	/**
	 * データがNULLかどうか判定する
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return boolean
	 */
	public boolean isNull(int row, int col) {
		return (getObject(row, col) == null);
	}

	/**
	 * データがNULLかどうか判定する
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return boolean
	 */
	public boolean isNull(int row, String columnName) {
		return (isNull(row, getColumnIndex(columnName)));
	}

	/**
	 * レコードイメージを配列で取得する
	 * 
	 * @param rowcount
	 * @return String[]
	 */
	public String[] getRow(int rowcount){
		String[] tmp = new String[colCount];
		int rownum = rowcount * colCount;
		
		for(int i = 0 ; i < colCount ; i++){
			if(result.get(i + rownum) == null) tmp[i] = null; 			
			else tmp[i] = "" + result.get(i + rownum);
		}
		
		return tmp;
	}
	
	/**
	 * レコードイメージを一列で取得する
	 * 
	 * @param rowcount
	 * @return String[]
	 */
	public String getRowLine(int rowcount){
		String tmp = "";
		int rownum = rowcount * colCount;
		
		for(int i = 0 ; i < colCount ; i++){
			tmp = tmp +result.get(i + rownum); 
		}		
		return tmp;
	}
	
	/**
	 * カラムのインデックス番号を取得する
	 * もしもカラムが存在しない場合は -1 が返る
	 * 
	 * @param columnName　列名
	 * @return int
	 */
	private int getColumnIndex(String columnName) {
		if (colName.containsKey(columnName)){
			return ((Integer)colName.get(columnName)).intValue();
		}else{
			return -1;
		}
	}
	

	/**
	 * Short型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return float
	 */
	public short getShort(int row, String columnName) {
		return (getShort(row, getColumnIndex(columnName)));
	}
	
	/**
	 * Short型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return float
	 */
	public short getShort(int row, int col) {
		if (isNull(row, col))
			return (0);

		Object o = getObject(row, col);
		if (o instanceof Short) {
			Short s = (Short) o;
			return (s.shortValue());
		} else if (o instanceof Integer) {
			Integer i = (Integer)o;
			return (i.shortValue());
		} else {
			BigDecimal b = (BigDecimal) o;
			return (b.shortValue());
		}
	}

	/**
	 * Short型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return Float
	 */
	public Short getShortObject(int row, String columnName) {
		return (getShortObject(row, getColumnIndex(columnName)));
	}
	
	/**
	 * Short型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return Float
	 */
	public Short getShortObject(int row, int col) {
		if (isNull(row, col))
			return null;

		Object o = getObject(row, col);
		if (o instanceof Short) {
			return ((Short) o);
		} else {
			return new Short(((BigDecimal) o).shortValue());
		}
	}
	

	/**
	 * byte[]型のデータ取得
	 * 
	 * @param row 行番号
	 * @param columnName 列名
	 * @return float
	 */
	public byte[] getByte(int row, String columnName) {
		return (getByte(row, getColumnIndex(columnName)));
	}
	
	/**
	 * byte[]型のデータ取得
	 * 
	 * @param row 行番号
	 * @param col 列番号
	 * @return float
	 */
	public byte[] getByte(int row, int col) {
		if (isNull(row, col)){
			return null;
		}

		Object o = getObject(row, col);
		return (byte[])o;

	}


}