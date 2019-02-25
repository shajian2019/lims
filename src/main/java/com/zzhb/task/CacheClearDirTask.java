package com.zzhb.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.config.Props;
import com.zzhb.utils.FileUtil;

@Service
public class CacheClearDirTask implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(CacheClearDirTask.class);

	@Autowired
	Props props;

	/**
	 * 删除缓存文件
	 */
	@Override
	public void run() {
		String tempPath = props.getTempPath();
		FileUtil.deleteDirectory(tempPath);

		logger.info("==================清除缓存文件夹下的文件======================" + tempPath);
	}

}
