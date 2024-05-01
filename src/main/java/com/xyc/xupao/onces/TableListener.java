package com.xyc.xupao.onces;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

// 有个很重要的点 TableListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class TableListener implements ReadListener<XuUserInfo> {

	/**
	 * 这个每一条数据解析都会来调用
	 *
	 * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
	 * @param context
	 */
	@Override
	public void invoke(XuUserInfo data, AnalysisContext context) {

	}

	/**
	 * 所有数据解析完成了 都会来调用
	 *
	 * @param context
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		// 这里也要保存数据，确保最后遗留的数据也存储到数据库
		saveData();
		log.info("所有数据解析完成！");
	}

	/**
	 * 加上存储数据库
	 */
	private void saveData() {
		log.error("更新失败");
	}
}
