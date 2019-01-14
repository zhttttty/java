package com.zhizhuotec.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;




import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhizhuotec.controller.UserController;

public class Junit1 extends JunitConfig {

	private MockMvc mockMvc;

	@Autowired
	private MockHttpSession session;

	@Autowired
	private UserController userController;

//	@Autowired
//	private StepNumberController stepNumberController;
//
//	@Autowired
//	private BloodOxygenContentController bloodOxygenContentController;
//
//	@Autowired
//	private BloodPressureController bloodPressureController;
//
//	@Autowired
//	private BreathingGuideController breathingGuideController;
//
//	@Autowired
//	private HeartRateController heartRateController;
//
//	@Autowired
//	private ElectrocardiogramController electrocardiogramController;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	// ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIFPSZRca2AGb305lWjMe7I2

	// int runnerCount = 10;
	// // Rnner数组，想当于并发多少个。
	// TestRunnable[] trs = new TestRunnable[runnerCount];
	// for (int j = 0; j < runnerCount; j++) {
	// trs[j] = runner();
	// }
	// // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
	// MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
	// try {
	// // 并发执行数组里定义的内容
	// long a = Util.getMillis();
	// mttr.runTestRunnables();
	// System.out.println("总计耗时 : " + String.valueOf(Util.getMillis() - a) +
	// "毫秒");
	// } catch (Throwable e) {
	// e.printStackTrace();
	// }

	@Test
	public void test001() throws Exception {
		
//		 mockMvc.perform(get("/user/login").param("identifier",
//		 "18715736535").param("credential", "p2791565"))
//		 .andDo(print());
		
		 mockMvc.perform(get("/bloodPressure/download")
				 .param("token",
				 "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIHz9f2B%g1u0vR1H8aYGB8abm")
				 .param("dates", String.valueOf(new
				 DateTime("2017-01-01").getMillis()))).andDo(print());
		 
//		 mockMvc.perform(put("/user/code")
//		 .param("identifier",
//		 "13569854754")
//		 .param("type", "1")).andDo(print());
		 
//		 mockMvc.perform(delete("/user/logout")
//				 .param("token",
//				 "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIH3wjsFGgymAYUBcL8w7IBm")
//				 .param("dates", String.valueOf(new
//				 DateTime("2017-01-01").getMillis()))).andDo(print());

//		Map<Long, Object> map10 = new HashMap<Long, Object>();
//		map10.put(System.currentTimeMillis(), "fwafwafwa");
//		map10.put(System.currentTimeMillis() + 25466465645l, "5565w");
//		map10.put(System.currentTimeMillis() + 788945565115l, "qqqqqqqgasgagawrrwrw");
//		String data = JSON.toJSONString(map10);
		// mockMvc.perform(post("/electrocardiogram/upload")
		// .param("token",
		// "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIFDzgMXd2B8oe30K2B%nHn8t6").param("data",
		// data))
		// .andDo(print());

//		mockMvc.perform(get("/electrocardiogram/download")
//				.param("token", "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIFDzgMXd2B8oe30K2B%nHn8t6")
//				.param("dates", Long.toString(System.currentTimeMillis()))).andDo(print());

//		for (int i = 0; i < 100; i++) {
//			 List<Long> dates = test();
//			 List<Object> list = new ArrayList<Object>();
//			 Map<String, Object> map10 = new HashMap<String, Object>();
//			 map10 = new HashMap<String, Object>();
//			 map10.put("dates", dates.get(0));
////			 map10.put("times", test0(dates.get(0)));
//			 map10.put("results", test3());
//			 list.add(map10);
//			 map10 = new HashMap<String, Object>();
//			 map10.put("dates", dates.get(1));
////			 map10.put("times", test0(dates.get(1)));
//			 map10.put("results", test3());
//			 list.add(map10);
//			 map10 = new HashMap<String, Object>();
//			 map10.put("dates", dates.get(2));
////			 map10.put("times", test0(dates.get(2)));
//			 map10.put("results", test3());
//			 list.add(map10);
//			 map10 = new HashMap<String, Object>();
//			 map10.put("dates", dates.get(3));
////			 map10.put("times", test0(dates.get(3)));
//			 map10.put("results", test3());
//			 list.add(map10);
//			 String data = JSON.toJSONString(list,
//			 SerializerFeature.DisableCircularReferenceDetect);
//
//			 mockMvc.perform(post("/stepNumber/upload")
//			 .param("token",
//			 "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIHEL0ZYfHl96AL2B%r5b5I5QW").param("data",
//			 data))
//			 .andDo(print());
//			 i++;
//		}


		// mockMvc.perform(post("/bloodOxygenContent/upload")
		// .param("token",
		// "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIFPSZRca2AGb305lWjMe7I2")
		// .param("data", data)).andDo(print());
		
//		 mockMvc.perform(put("/user/code").param("identifier",
//		 "13333333333").param("type", "2"))
//		 .andDo(print());
		
//		 mockMvc.perform(post("/user/register").param("identifier",
//		 "13333333333").param("credential", "p2791565").param("code", "666666"))
//		 .andDo(print());

//		 mockMvc.perform(get("/user/login").param("identifier",
//		 "18715736535").param("credential", "p2791565"))
//		 .andDo(print());
		//
		// mockMvc.perform(get("/user/login").param("identifier",
		// "18715736536").param("credential", "p2791565"))
		// .andDo(print());

		// mockMvc.perform(post("/family/sendNotification")
		// .param("token",
		// "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIGZfruCjfpKzvnvRebgCeII")
		// .param("familyPhone", "18715736536").param("msg",
		// "18715736535请求加您为家庭成员1")).andDo(print());
		//
		// mockMvc.perform(post("/family/sendNotification")
		// .param("token",
		// "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIGZfruCjfpKzvnvRebgCeII")
		// .param("familyPhone", "18715736536").param("msg",
		// "18715736535请求加您为家庭成员2")).andDo(print());
		//
		// mockMvc.perform(get("/family/getNotificationList").param("token",
		// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ6TP0oQzUlIEiU+bGkKHKWP")).andDo(print());

		// mockMvc.perform(post("/family/addFamily")
		// .param("token",
		// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ6TP0oQzUlIEiU+bGkKHKWP")
		// .param("familysId",
		// "778eacaad766495294b2c1598c894e38").param("status",
		// "1").param("number",
		// "4")).andDo(print());

//		 mockMvc.perform(get("/family/getFamilyList").param("token",
//		 "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIEp1BtMNtdZplwJN5XbBbsu")).andDo(print());

		// mockMvc.perform(put("/family/updateRemark")
		// .param("token",
		// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ6TP0oQzUlIEiU+bGkKHKWP")
		// .param("familysId",
		// "778eacaad766495294b2c1598c894e38").param("remark",
		// "fwioafiowaiooi和哦哦IE搜我啊*（*……%%￥￥%￥#@@&（））++")).andDo(print());

		// mockMvc.perform(delete("/family/deletFamily")
		// .param("token",
		// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ6TP0oQzUlIEiU+bGkKHKWP")
		// .param("familysId",
		// "778eacaad766495294b2c1598c894e38")).andDo(print());

		// mockMvc.perform(post("/breathingGuide/upload")
		// .param("token",
		// "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIFPSZRca2AGb305lWjMe7I2")
		// .param("data", data)).andDo(print());
		//
		// mockMvc.perform(post("/stepNumber/upload")
		// .param("token",
		// "ccX62iN4paKuZhkbT7mXieAor7PqEVFCf54if5WbWIFPSZRca2AGb305lWjMe7I2")
		// .param("data", data)).andDo(print());
	}
	//
	// @Test
	// @Rollback(value = false)
	// public void test002() throws Exception {
	//
	// mockMvc.perform(get("/heartRate/download")
	// .param("token",
	// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ52S0Rqnh7zEBqGQ9wRTnJH")
	// .param("dates", String.valueOf(new
	// DateTime("2018-01-01").getMillis()))).andDo(print());
	//
//	 mockMvc.perform(get("/bloodOxygenContent/download")
//	 .param("token",
//	 "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ52S0Rqnh7zEBqGQ9wRTnJH")
//	 .param("dates", String.valueOf(new
//	 DateTime("2018-01-01").getMillis()))).andDo(print());
	//
	// mockMvc.perform(get("/bloodPressure/download")
	// .param("token",
	// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ52S0Rqnh7zEBqGQ9wRTnJH")
	// .param("dates", String.valueOf(new
	// DateTime("2018-01-01").getMillis()))).andDo(print());
	//
	// mockMvc.perform(get("/breathingGuide/download")
	// .param("token",
	// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ52S0Rqnh7zEBqGQ9wRTnJH")
	// .param("dates", String.valueOf(new
	// DateTime("2018-01-01").getMillis()))).andDo(print());
	//
	// mockMvc.perform(get("/stepNumber/download")
	// .param("token",
	// "f0iWgdkXqsq76xZedXqCp0YV1SHtgmWy9bFEz8SqoQ52S0Rqnh7zEBqGQ9wRTnJH")
	// .param("dates", String.valueOf(new
	// DateTime("2018-01-01").getMillis()))).andDo(print());
	// }

	private List<Long> test() {
		DateTime dt = new DateTime();
		List<Long> list = new ArrayList<Long>();
		List<Long> list1 = new ArrayList<Long>();
		while (list1.size() < 4) {
			list = new ArrayList<Long>();
			list.add(new DateTime(
					dt.dayOfMonth().withMinimumValue().plusDays((int) (Math.random() * 60)).toString("yyyy-MM-dd"))
							.getMillis());
			list.add(new DateTime(
					dt.dayOfMonth().withMinimumValue().plusDays((int) (Math.random() * 60)).toString("yyyy-MM-dd"))
							.getMillis());
			list.add(new DateTime(
					dt.dayOfMonth().withMinimumValue().plusDays((int) (Math.random() * 60)).toString("yyyy-MM-dd"))
							.getMillis());
			list.add(new DateTime(
					dt.dayOfMonth().withMinimumValue().plusDays((int) (Math.random() * 60)).toString("yyyy-MM-dd"))
							.getMillis());
			list1 = new ArrayList<Long>();
			for (Long s : list) {
				if (!list1.contains(s)) {
					list1.add(s);
				}
			}
		}
		return list1;
	}

	private String test0(Long date) {
		List<Long> list = new ArrayList<Long>();
		int r = ((int) (Math.random() * 29)) + 1;
		for (int i = 0; i < r; i++) {
			list.add(date + ((int) (Math.random() * 10000)));
		}
		return JSON.toJSONString(list);
	}

	private String test1() {
		List<Integer> list = new ArrayList<Integer>();
		int r = ((int) (Math.random() * 29)) + 1;
		for (int i = 0; i < r; i++) {
			list.add(((int) (Math.random() * 40)) + 40);
		}
		return JSON.toJSONString(list);
	}

	private String test2() {
		String ecg = "";
		int r = ((int) (Math.random() * 1000)) + 9000;
		for (int i = 0; i < r; i++) {
			ecg += (((int) (Math.random() * 100)) + 10) + ",";
		}
		return ecg;
	}

	private String test3() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			list.add((((int) (Math.random() * 1000)) + 200));
		}
		return JSON.toJSONString(list);
	}

	private String test4() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			list.add(((((int) (Math.random() * 110)) + 40) * 1000) + (((int) (Math.random() * 190)) + 60));
		}
		return JSON.toJSONString(list);
	}
	
	// private TestRunnable runner() {
	// // 测试内容
	// TestRunnable runner = new TestRunnable() {
	// @Override
	// public void runTest() throws Throwable {
	// String identifier = "";
	// for (int i = 0; i < 11; i++) {
	// if (i == 0) {
	// identifier += (int) (Math.random() * 9) + 1;
	// } else {
	// identifier += (int) (Math.random() * 10);
	// }
	// }
	// String registerIp = "";
	// for (int i = 0; i < 11; i++) {
	// if (i == 2) {
	// registerIp += ".";
	// } else if (i == 5) {
	// registerIp += ".";
	// } else if (i == 8) {
	// registerIp += ".";
	// } else {
	// registerIp += (int) (Math.random() * 3);
	// }
	// }
	// UserAuths userAuths = new UserAuths();
	// userAuths.setIdentifier(identifier);
	// userAuths.setCredential("p2791565");
	// userAuths.setRegisterIp(registerIp);
	//
	// Map<String, Object> map2 = userController.registerCode(userAuths);
	// // for (Map.Entry<String, Object> entry : map2.entrySet()) {
	// // System.out.println("2Key = " + entry.getKey() + ", Value
	// // = " + entry.getValue());
	// // }
	//
	// Map<String, Object> map3 = userController.register(userAuths, "666666");
	// // for (Map.Entry<String, Object> entry : map3.entrySet()) {
	// // System.out.println("3Key = " + entry.getKey() + ", Value
	// // = " + entry.getValue());
	// // }
	// }
	// };
	// return runner;
	// }

}
