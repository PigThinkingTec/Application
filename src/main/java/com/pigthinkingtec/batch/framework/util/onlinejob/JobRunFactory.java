package com.pigthinkingtec.batch.framework.util.onlinejob;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.util.onlinejob.JobToolInterface;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;

/**
 * 定ファイルの設定によって自動的にジョブを起動するツールのインスタンスを作成して、ジョブまたはジョブチェーンを起動するクラス
 * @author yizhou
 *
 */
public class JobRunFactory {

	private final static Logger logger = LoggerFactory.getLogger(JobRunFactory.class);
	
	private final static String startTimeNow = "now";
	
    private static JobToolInterface   jobTool = null;
 
    private static JobToolConnectInfo connInfo = null;
    
    /**
     * 設定ファイルの設定によって自動的にジョブを起動するツールのインスタンスを作成する
     */
    private static JobToolInterface getJobTool() throws SystemException{
		// framework.propertiesファイルから、Jobツール名前を取得する
		String jobToolName=PropertiesUtil.getProperty(SystemConstant.JOB_TOOL_NAME_KEY);
		
		logger.debug("jobToolName = " + jobToolName);
		
		if (jobToolName == null) {
			throw new SystemException("The key[" + SystemConstant.JOB_TOOL_NAME_KEY + 
					                      "] is empty. Please set it in framework.properties file.");
		}
		
		// Jobツール名前によって、違うツールのFactoryを作成して返す
		if (SystemConstant.JOB_TOOL_NAME_SOS_JOBSCHEDULER.equals(jobToolName.toUpperCase())) {
			//SOS JobSchedulerのインスタンスを作成する
			jobTool = new SOSJobSchedulerTool();
		} else if (SystemConstant.JOB_TOOL_NAME_JOBCENTER.equals(jobToolName.toUpperCase())) {
			//JobCenterのインスタンスを作成する
			jobTool = new JobCenterTool();
		} else {
			throw new SystemException("The key[" + SystemConstant.JOB_TOOL_NAME_KEY + 
					"]'s value is incorrect. Please correct it in framework.properties file.");
		}
		
		return jobTool;
    }
    
    
    /**
     * サーバ設定情報を初期化する
     * @param server 				JobSchedulerツールがインストールされているサーバ名またはIPアドレス。
     *                              (例：localhost, 192.168.33.44)<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B>  
     *                              <B><font color='red'>必須</font></B>。
     * @param port 					JobSchedulerツールのポート。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> <B>
     *                              <font color='red'>必須</font></B>。
     * @param user 					JobSchedulerツールのユーザ。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> <B>非必須</B>。
     * @param password 				JobSchedulerツールのパースワード。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> <B>非必須</B>。
     * @param webServiceUrlPattern  起動する対象ジョブツールのWebserviceのURLパターン。
     *                              <B><font color='blue'>（設定しない場合framework.propertiesファイルから取得）</font></B>
     *                              <B><font color='red'>必須</font></B>。
     *                              <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *                              <font color='green'>SOS-JobSchedulerの場合、scheduler.xmlのweb_service節に定義されたurl_pathの値をここで指定してください。</font>
     * @param starttime				起動時刻。設定しない場合即時起動とする。
     *                              <font color='blue'><B>YYYY-MM-DD hh:mm:ssの形で設定する。
     *                              </B></font> <B>非必須</B>。
     * @param parasMap				ジョブを実行するパラメータ。
     * @return
     * @throws SystemException
     */
    private static JobToolConnectInfo initJobToolConnectInfo(String server, 
    		                                                 String port, 
    		                                                 String user, 
    		                                                 String password, 
    		                                                 String webServiceUrlPattern, 
    		                                                 String starttime, 
    		                                                 Map<String, OnlineJobParaBean> parasMap) throws SystemException{   	
    	//サーバ情報設定(必須)
    	if (StringUtil.isEmpty(server)) {
    		server = PropertiesUtil.getProperty(SystemConstant.JOB_TOOL_SERVER);
    	}
    	
		if (server == null) {
			throw new SystemException("The key[" + SystemConstant.JOB_TOOL_SERVER + 
					                     "] is empty. Please set it in framework.properties file.");
		}
		
		//PORT情報設定(必須)
    	if (StringUtil.isEmpty(port)) {
    		port = PropertiesUtil.getProperty(SystemConstant.JOB_TOOL_PORT);
    	}
    	
		if (port == null) {
			throw new SystemException("The key[" + SystemConstant.JOB_TOOL_PORT + 
					                     "] is empty. Please set it in framework.properties file.");
		}
		
		
		//ユーザ名設定(非必須)
    	if (StringUtil.isEmpty(user)) {
    		user = PropertiesUtil.getProperty(SystemConstant.JOB_TOOL_USER);
    	}
    	
    	//パースワード設定(非必須)
    	if (StringUtil.isEmpty(password)) {
    		password = PropertiesUtil.getProperty(SystemConstant.JOB_TOOL_PASSWORD);
    	}
    	
    	//webservice名を設定(JobtoolはSOS　JobSchedulerの場合のみ必須。他のJobToolの場合不要)
    	if (StringUtil.isEmpty(webServiceUrlPattern)) {
    		webServiceUrlPattern = PropertiesUtil.getProperty(SystemConstant.JOB_TOOL_WEBSERVICE);
    		
    		String jobToolName=PropertiesUtil.getProperty(SystemConstant.JOB_TOOL_NAME_KEY);
    		
    		if (SystemConstant.JOB_TOOL_NAME_SOS_JOBSCHEDULER.equals(jobToolName.toUpperCase())) {
    			//JobtoolはSOS　JobSchedulerの場合のみ,Webservice名が必須であるため。設定されない場合エラーとする
    			if (StringUtil.isEmpty(webServiceUrlPattern)){
    				throw new SystemException("The key[" + SystemConstant.JOB_TOOL_WEBSERVICE + 
    						                      "] is necessary but it's empty when the jobtool is " + 
    						                      SystemConstant.JOB_TOOL_NAME_SOS_JOBSCHEDULER + ".");
    			}
    		}
    	}
    	
    	if (StringUtil.isEmpty(starttime)) {
    		//起動時間が設定されていない場合、nowと見なして即時起動とする。
    		starttime = startTimeNow; 
    		
    	}else if (startTimeNow.equals(starttime)  || StringUtil.contains(starttime, startTimeNow)){
    		//起動時間がnow、
    		//またはnow+20のように設定されている場合、処理しないこと。
    		;
    		
    	}else {
    		// 起動時刻が設定されている場合、
    		// YYYY-MM-DD hh:mm:ssのフォーマットになっているかどうかをチェックする。
    		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    		try {
    		    format.parse(starttime);//成功
    		} catch (ParseException e) {
    		    //失敗時の処理…
    			throw new SystemException("The format of starttime is not 'YYYY-MM-DD hh:mm:ss'.  starttime = [" +
                                             starttime + "]." + 
    		                                 e.getStackTrace());
    		}
    	}
    	
    	OnlineJobParaBean[] paraBeans = new OnlineJobParaBean[0];
    	
    	if (parasMap == null || parasMap.isEmpty() || parasMap.size() == 0) {
    		paraBeans = new OnlineJobParaBean[0];
    	} else {
    		paraBeans = parasMap.values().toArray(new OnlineJobParaBean[0]);
    	}
    	
    	
    	//接続インスタンスを初期化する
    	connInfo = new JobToolConnectInfo(server,
    			                      Integer.parseInt(port),
    			                      user,
    			                      password,
    			                      webServiceUrlPattern,
    			                      starttime,
    			                      paraBeans);
    	
    	//作成した接続情報インスタンスを返す
    	return connInfo;
    	
    }
    
    /**
     * ジョブを起動する<br>
	 * <font color='blue'><B>注意：複数場所（例：複数ユーザで複数画面）からこのメソッドを経由して同一ジョブ(jobPathは同じである)を起動するかつ、<br>
	 * 起動したジョブ達を起動順番で直列に実行したい（並行実行禁止)場合、対象ジョブの定義に、直列実行するための特殊な設定は必要かもしれない。<br>
	 * 詳細は、対象ジョブツール（例：SOS JobScheduler、JobCenter、JP1など）のドキュメントを参照してください。<br>
	 * 例：SOS JobSchedulerの場合、対象ジョブのxmlに、tasks="1"の指定が必須。</B></font>
     * @param server 				JobSchedulerツールがインストールされているサーバ名またはIPアドレス。
     *                              (例：localhost, 192.168.33.44)<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B>  
     *                              <B><font color='red'>必須</font></B>。
     * @param port 					JobSchedulerツールのポート。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> <B>
     *                              <font color='red'>必須</font></B>。
     * @param user 					JobSchedulerツールのユーザ。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> <B>非必須</B>。
     * @param password 				JobSchedulerツールのパースワード。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> <B>非必須</B>。
     * @param webServiceUrlPattern  起動する対象ジョブツールのWebserviceのURLパターン。
     *                              <B><font color='blue'>（設定しない場合framework.propertiesファイルから取得）</font></B>
     *                              <B><font color='red'>必須</font></B>。
     *                              <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *                              <font color='green'>SOS-JobSchedulerの場合、scheduler.xmlのweb_service節に定義されたurl_pathの値をここで指定してください。</font>
     * @param jobid					起動する対象ジョブのジョブID。<B><font color='red'>必須</font></B>。
     * @param starttime				対象ジョブの起動時刻。設定しない場合、デフォルトとして即時起動とする。
     *                              <font color='blue'><B>YYYY-MM-DD hh:mm:ssまたは、now、now+30のような形で設定する。
     *                              </B></font> <B>非必須</B>。
     * @param parasMap				ジョブを実行するパラメータ。
     *                              
     * @return  0: 正常　　<br> -1: エラー 
     * @throws SystemException
     */
    public static int runJob(String server, 
    		                 String port, 
    		                 String user, 
    		                 String password, 
    		                 String webServiceUrlPattern, 
    		                 String jobid, 
    		                 String starttime, 
    		                 Map<String, OnlineJobParaBean> parasMap) throws SystemException{
    	if (jobTool == null ){
    		jobTool = getJobTool();
		}
		
    	//接続情報を初期化する
    	JobToolConnectInfo info = initJobToolConnectInfo(server, 
    			                                         port, 
    			                                         user, 
    			                                         password, 
    			                                         webServiceUrlPattern, 
    			                                         starttime, 
    			                                         parasMap);
    	
    	logger.debug("runJob before. ");
    	
    	//ジョブを起動する
		return jobTool.runJob(info, jobid);
    }
    
    
    /**
	 * ジョブチェーンを起動する<br>
	 * <font color='blue'><B>注意：複数場所（例：複数ユーザで複数画面）からこのメソッドを経由して同一ジョブチェーン(jobChainPathは同じである)を起動するかつ、<br>
	 * 起動したジョブチェーンを起動順番で直列に実行したい（並行実行禁止)場合、対象ジョブチェーンの定義に、直列実行するための特殊な設定は必要かもしれない。<br>
	 * 詳細は、対象ジョブツール（例：SOS JobScheduler、JobCenter、JP1など）のドキュメントを参照してください。<br>
	 * 例：SOS JobSchedulerの場合、対象ジョブチェーンのxmlに、max_orders="1"の指定が必須。</B></font>
     * @param server 				JobSchedulerツールがインストールされているサーバ名またはIPアドレス。
     *                              (例：localhost, 192.168.33.44)<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B>  
     *                              <B><font color='red'>必須</font></B>。
     * @param port 					JobSchedulerツールのポート。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> 
     *                              <B><font color='red'>必須</font></B>。
     * @param user 					JobSchedulerツールのユーザ。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）</font></B> 
     *                              <B>非必須</B>。
     * @param password 				JobSchedulerツールのパースワード。<B><font color='blue'>
     *                              （設定しない場合framework.propertiesファイルから取得）
     *                              </font></B> <B>非必須</B>。
     * @param webServiceUrlPattern  起動する対象ジョブツールのWebserviceのURLパターン。
     *                              <B><font color='blue'>（設定しない場合framework.propertiesファイルから取得）</font></B>
     *                              <B><font color='red'>必須</font></B>。
     *                              <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *                              <font color='green'>SOS-JobSchedulerの場合、scheduler.xmlのweb_service節に定義されたurl_pathの値をここで指定してください。</font>
     * @param jobchainid			起動する対象ジョブチェーンのID。<B><font color='red'>必須</font></B>。
     * @param starttime				対象ジョブチェーンの起動時刻。設定しない場合即時起動とする。
     *                              <font color='blue'><B>YYYY-MM-DD hh:mm:ssまたは、now、now+30のような形で設定する。</B></font> 
     *                              <B>非必須</B>。
     * @param parasMap				ジョブを実行するパラメータ。
     * @return  0: 正常　　<br> -1: エラー 
     * @throws SystemException
     */
    public static int runJobChain(String server, 
    		                      String port, 
    		                      String user, 
    		                      String password, 
    		                      String webServiceUrlPattern,  
    		                      String jobchainid, 
    		                      String starttime,
    		                      Map<String, OnlineJobParaBean> parasMap) throws SystemException{
    	if (jobTool == null ){
    		jobTool = getJobTool();
		}
		
    	//接続情報を初期化する
    	JobToolConnectInfo info = initJobToolConnectInfo(server, 
    			                                         port, 
    			                                         user, 
    			                                         password, 
    			                                         webServiceUrlPattern,
    			                                         starttime,
    			                                         parasMap);
    	
    	//ジョブチェーンを起動する
		return jobTool.runJobChain(info, jobchainid);
    }
    

    
}