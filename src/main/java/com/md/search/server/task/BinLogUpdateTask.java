package com.md.search.server.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.md.search.server.bean.TableTask;
import com.md.search.server.service.OperateService;
import com.md.search.server.util.TableTaskUtil;

/**
 * 处理更新任务的任务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午5:31:50
 */
public class BinLogUpdateTask implements Runnable {

	public static Logger logger = LoggerFactory
			.getLogger(BinLogUpdateTask.class);

	TableTask tableTask = null;

	public static OperateService merchantService;

	@Override
	public void run() {

		while (true) {
			try {
				tableTask = TableTaskUtil
						.getEventTask(EventType.EXT_UPDATE_ROWS);
				if (tableTask.getTable().contains("data_resource")) {
					for (String id : tableTask.getIds()) {
						EventHeaderV4 header = (EventHeaderV4) tableTask
								.getHeader();
						merchantService.eventControl(tableTask.getEventType(),
								id, tableTask.getTable(),
								header.getNextPosition());
					}
				}
			} catch (InterruptedException e) {
				logger.error("binlog update task error:", e);
			}

		}

	}
}
