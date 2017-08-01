package com.pigthinkingtec.batch.framework.util.onlinejob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.util.onlinejob.JobToolInterface;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.StringUtil;

import java.io.*;
import javax.xml.soap.*;

/**
 * SOS-JobScheduler WebServiceを経由してジョブ、ジョブチェーンを起動するクラス
 * <br><B><font color="red">SOS JobSchedulerのバージョンは1.7.4169以上必要</font></B>
 * @author yizhou
 *
 */
public class SOSJobSchedulerTool implements JobToolInterface{
	
	private final static Logger logger = LoggerFactory.getLogger(SOSJobSchedulerTool.class);
	
	private final static String URL_PREFIX_HTTP = "http://";
	
	private final static String SOAP_REQUEST_BODY_PREFIX = new StringBuilder(
															"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
															"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
																"<soapenv:Body>" 
														).toString();
	
	private final static String SOAP_REQUEST_BODY_START_JOB_PREFIX = "<startJob xmlns=\"http://www.sos-berlin.com/scheduler\">" ;
	private final static String SOAP_REQUEST_BODY_START_JOB_SUFFIX = "</startJob>" ;
	
	private final static String SOAP_REQUEST_BODY_START_JOB_JOBTAG_START = "<job>" ;
	private final static String SOAP_REQUEST_BODY_START_JOB_JOBTAG_END = "</job>" ;
	
	private final static String SOAP_REQUEST_BODY_START_JOB_ATTAG_START = "<at>" ;
	private final static String SOAP_REQUEST_BODY_START_JOB_ATTAG_END = "</at>" ;
	
	private final static String SOAP_REQUEST_BODY_ADD_ORDER_PREFIX = "<addOrder xmlns=\"http://www.sos-berlin.com/scheduler\">" ;
	private final static String SOAP_REQUEST_BODY_ADD_ORDER_SUFFIX = "</addOrder>" ;
	
	private final static String SOAP_REQUEST_BODY_START_ORDER_PARATAG_START = "<param>" ;
	private final static String SOAP_REQUEST_BODY_START_ORDER_PARATAG_END = "</param>" ;
	
	private final static String SOAP_REQUEST_BODY_START_ORDER_PARA_NAMETAG_START = "<name>" ;
	private final static String SOAP_REQUEST_BODY_START_ORDER_PARA_NAMETAG_END = "</name>" ;
	
	private final static String SOAP_REQUEST_BODY_START_ORDER_PARA_VALUETAG_START = "<value>" ;
	private final static String SOAP_REQUEST_BODY_START_ORDER_PARA_VALUETAG_END = "</value>" ;
	
	private final static String SOAP_REQUEST_BODY_SUFFIX = new StringBuilder(
																"</soapenv:Body>" +
															"</soapenv:Envelope>"
														).toString();
	
	/**
	 * 対象ジョブツール（SOS JobScheduler）のWebServiceを経由してジョブを起動する<br>
	 * <font color='blue'><B>注意：複数場所（例：複数ユーザで複数画面）からこのメソッドを経由して同一ジョブ(jobPathは同じである)を起動するかつ、<br>
	 * 起動したジョブを起動順番で直列に実行したい（並行実行禁止)場合、対象ジョブの定義に、tasks="1"の指定が必須。<br>
	 * 詳細は、対象ジョブツール（SOS JobScheduler）のドキュメントを参照してください。<br></font>
	 * <br><B><font color="red">また、本機能を使うためにSOS JobSchedulerのバージョンは1.7.4169以上必須。</font></B>
	 * @param connInfo	      ジョブツールのWebServiceを接続するパラメータクラス、このクラス中にジョブまたはジョブを実行するパラメータが含まる。
	 * @param jobPath 起動する対象ジョブのID。パス情報が必要な場合、纏めてこのパラメータに指定してください。<br>
	 *                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *                例： JobSchedulerツールの場合：..../live/testJob/配下にhelloJob.job.xmlジョブがある場合、testJob/helloJob に指定してください。
	 * @return 0:正常終了　-1:エラー終了
	 */
	@Override
	public int runJob(JobToolConnectInfo connInfo, String jobPath) throws SystemException {

		// http://localhost:4444/webservices のようなURLを作成する。
		String webservice_url = URL_PREFIX_HTTP + 
			                    connInfo.getServer() + ":" + 
				                connInfo.getPort() + "/" + 
			                    connInfo.getWebserviceUrlPattern();
		
		// 下記のようなパラメータのStringを作成する
		//"<param>" + 
		//    "<name>" + "name1" + "</name>" + 
		//    "<value>" + "value1" + "</value>" +  
		//"</param>" + 	
		String paras = createParas(connInfo);
		
		// 下記のように、Jobを起動するSOAP　Request　MSGを作成する
//		----------------------------------------------------------------------------
//		<?xml version="1.0" encoding="UTF-8"?>
//			<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
//			<soapenv:Body>
//				<startJob xmlns="http://www.sos-berlin.com/scheduler">
//					<job>xxxxx</job>
//					<at>now</at>
//				</startJob>
//			</soapenv:Body>
//		</soapenv:Envelope>
//		----------------------------------------------------------------------------
		StringBuilder soapMsg = new StringBuilder(
								SOAP_REQUEST_BODY_PREFIX +
									SOAP_REQUEST_BODY_START_JOB_PREFIX +
										//起動対象ジョブ名を指定する。liveフォルダ以降のパス情報があれば含めて指定してください。
									    //例：　./live/sample/test/sampleJobの場合、
									    //          sample/test/sampleJobに設定してください。
									     
									    //<job>xxxxx</job>
										SOAP_REQUEST_BODY_START_JOB_JOBTAG_START + 
										  jobPath + 
										SOAP_REQUEST_BODY_START_JOB_JOBTAG_END +    
                                        
										//起動時刻 <at>now</at>
									    SOAP_REQUEST_BODY_START_JOB_ATTAG_START + 
									      connInfo.getStarttime() + 
									    SOAP_REQUEST_BODY_START_JOB_ATTAG_END +

									    //"<param>" + 
									    //    "<name>" + "name1" + "</name>" + 
									    //    "<value>" + "value1" + "</value>" +  
									    //"</param>" + 	  
									    paras +
									    
									SOAP_REQUEST_BODY_START_JOB_SUFFIX +
								SOAP_REQUEST_BODY_SUFFIX
								);
		
		
		//ジョブを起動する。
		return run(connInfo, soapMsg.toString(), webservice_url);
		
	}

	
	
	/**
	 * 対象ジョブツール（SOS JobScheduler）のWebServiceを経由してジョブチェーンを起動する<br>
	 * <font color='blue'><B>注意：複数場所（例：複数ユーザで複数画面）からこのメソッドを経由して同一ジョブチェーン(jobChainPathは同じである)を起動するかつ、<br>
	 * 起動したジョブチェーンを起動順番で直列に実行したい（並行実行禁止)場合、対象ジョブチェーンの定義に、max_orders="1"の指定が必須。<br>
	 * 詳細は、対象ジョブツール（SOS JobScheduler）のドキュメントを参照してください。<br></font>
	 * <br><B><font color="red">また、本機能を使うためにSOS JobSchedulerのバージョンは1.7.4169以上必須。</font></B>
	 * @param connInfo	      ジョブツールのWebServiceを接続するパラメータクラス、このクラス中にジョブまたはジョブチェーンを実行するパラメータが含まる。
	 * @param jobChainPath 起動する対象ジョブのID。パス情報が必要な場合、纏めてこのパラメータに指定してください。<br>
	 *                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *                例： JobSchedulerツールの場合：..../live/testJobChain/配下にhellochain.jobchain.xmlジョブがある場合、testJobChain/hellochain に指定してください。
	 * @return 0:正常終了　-1:エラー終了
	 */
	@Override
	public int runJobChain(JobToolConnectInfo connInfo, String jobChainPath) throws SystemException {
		
		if (StringUtil.isEmpty(jobChainPath)){
			logger.error("jobChainPath is null or jobChainPath.length == 0. "); 
			return -1;
		}
			
		// 下記のようなパラメータのStringを作成する
		//"<param>" + 
		//    "<name>" + "name1" + "</name>" + 
		//    "<value>" + "value1" + "</value>" +  
		//"</param>" + 	
		String paras = createParas(connInfo);
		
		// http://localhost:4444/webservices のようなURLを作成する。
		String webservice_url = URL_PREFIX_HTTP + 
			                    connInfo.getServer() + ":" + 
				                connInfo.getPort() + "/" + 
			                    connInfo.getWebserviceUrlPattern();
		
		// 下記のように、Jobchainを起動するSOAP　Request　MSGを作成する
//		<?xml version="1.0" encoding="UTF-8"?>
//		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
//		   <soapenv:Body>
//		      <addOrder xmlns="http://www.sos-berlin.com/scheduler">
//		        <!-- Job Chain name with full qualified path after live folder-->
//		         <jobChain>examples/01_JobChainShellJobs/01_JobChainA</jobChain>
//		         <title>soaptest</title>
//		         <!-- parameters passed to the job chain-->
//		         <param>
//		            <name>SOAP_PARAM1</name>
//		            <value>Value1</value>
//		         </param>
//		         <param>
//		            <name>SOAP_PARAM2</name>
//		            <value>Value2</value>
//		         </param>
//		      </addOrder>
//		   </soapenv:Body>
//		</soapenv:Envelope>
		StringBuilder soapMsg = new StringBuilder(
				SOAP_REQUEST_BODY_PREFIX +
					SOAP_REQUEST_BODY_ADD_ORDER_PREFIX +
					     //起動対象ジョブチェーン名を指定する。liveフォルダ以降のパス情報があれば含めて指定してください。
					     //例：　./live/sample/test/sampleJobChainの場合、
					     //           sample/test/sampleJobChainに設定してください。
						"<jobchain>" + jobChainPath + "</jobchain>"  +  
					     
                         //パラメータがある場合、下記のように追加する
						//"<param>" + 
						//    "<name>" + "key1" + "</name>" + 
						//    "<value>" + "value1" + "</value>" +  
						//"</param>" 
						paras +
					SOAP_REQUEST_BODY_ADD_ORDER_SUFFIX +
				SOAP_REQUEST_BODY_SUFFIX
				);
		
		
		//ジョブを起動する。
		return run(connInfo, soapMsg.toString(), webservice_url);
	}

		
	
	//webservice接続して、起動SOAPメッセージを送信する
	private int run(JobToolConnectInfo connInfo, String soapMsg, String webservice_url) throws SystemException {

		SOAPConnectionFactory scf = null;
		SOAPConnection conn = null;
		// 戻り値
		int ret = 0;
		
		logger.info("run() start. soapMsg = " + soapMsg + ". "); 
		
		try {			
			scf = SOAPConnectionFactory.newInstance();
			conn = scf.createConnection();
						
			//Convert String SOAP Request Message to Byte SOAPMessage
			MessageFactory factory = MessageFactory.newInstance();
			SOAPMessage soapRequest = factory.createMessage(null,new ByteArrayInputStream(soapMsg.getBytes()));

			logger.info("conn.call() before. webservice_url = " + webservice_url); 
			
			// ジョブを起動する
			// 同期起動・非同期起動と関わらず、キック自体成功か、失敗かの結果が先に戻ってくる、
			// この結果によって判定する
			SOAPMessage reply = conn.call(soapRequest, webservice_url);

			logger.info("conn.call() after.");
			
			// 戻り値取得
			SOAPPart part = reply.getSOAPPart();
			SOAPEnvelope env = part.getEnvelope();
			SOAPBody responseBody = env.getBody();
			
			// 戻り値判定
			if (responseBody.hasFault()) {			
				SOAPFault fault = responseBody.getFault();							
				
				logger.error("SOS JobScheduler webservice run failed." +
				                 "faultActor = " +  fault.getFaultActor() + 
				                 " faultCode = " + fault.getFaultCode() + 
				                 " faultString = " + fault.getFaultString());

				ret = -1;
				
			} else {
				logger.debug("SOS JobScheduler webservice run successfully.");
				ret = 0;
				
			}

		} catch (SOAPException e) {
			logger.error("SOAPException raised." +
	                 				"Message: " +  e.getMessage() + 
	                 				" Cause: " + e.getCause() + 
	                 				" " + e.getStackTrace());
			ret = -1;
			
		} catch (Throwable t) {
			logger.error("Throwable exception occured in run()." + t.getStackTrace());
			ret = -1;
			
		} finally {
			// 接続をクローズする
	        if (conn != null) {
	        	try{
	        		conn.close();
	        	}catch(Exception e){
	        		logger.error("Error occured in finally section.");
	        		throw new SystemException("SOAP connection close failed." + e.getMessage());
	        	}
	        }
	    }

		logger.info("run() end. ret = " + ret); 
		return ret;
	}
	
	/**
	 * ジョブまたはジョブチェーンのパラメータを作成する
	 * @param connInfo JobSchedulerツールのWebServiceを接続するパラメータクラス、このクラス中にジョブまたはジョブチェーンを実行するパラメータが含まる。
	 * @return
	 */
	private String createParas(JobToolConnectInfo connInfo){
		StringBuilder paras = new StringBuilder("");
		
		if (connInfo.getParaBeans() == null || connInfo.getParaBeans().length == 0) {
			logger.info("connInfo.getParaBeans() is empty.");
		} else {
			// 下記のようなパラメータのStringを作成する
			//"<param>" + 
			//    "<name>" + "name1" + "</name>" + 
			//    "<value>" + "value1" + "</value>" +  
			//"</param>" + 	
			for (int i = 0; i < connInfo.getParaBeans().length; i ++){
				if (StringUtil.isEmpty(connInfo.getParaBeans()[i].getParaName())) {
					//パラメータ名が設定されないパラメータは無視する
					logger.warn("The parameter's name is empty. Skip!!!");
					continue;
				}
				
				paras.append(SOAP_REQUEST_BODY_START_ORDER_PARATAG_START +
						        SOAP_REQUEST_BODY_START_ORDER_PARA_NAMETAG_START + 
						            connInfo.getParaBeans()[i].getParaName() +  
						        SOAP_REQUEST_BODY_START_ORDER_PARA_NAMETAG_END +
						        SOAP_REQUEST_BODY_START_ORDER_PARA_VALUETAG_START + 
						            connInfo.getParaBeans()[i].getParaValue() +  
						        SOAP_REQUEST_BODY_START_ORDER_PARA_VALUETAG_END +
						      SOAP_REQUEST_BODY_START_ORDER_PARATAG_END);
			}
			logger.info("paras = " + paras.toString());
		}
		
		return paras.toString();
	}
}
