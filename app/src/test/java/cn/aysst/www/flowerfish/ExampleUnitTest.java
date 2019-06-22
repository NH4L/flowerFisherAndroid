package cn.aysst.www.flowerfish;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void Test() {
        String a = "{\"answercount\": 9, \"answer\": [{\"content\": \"2017年12月29日 - 河海大学(Hohai University),简称“河海”,位于江苏省会南京市,是一所有百年办学历史,以水利为特色,工科为主,多学科协调发展的教育部直属,教育部、水...\", \"url\": \"http://www.baidu.com/link?url=cCJPSNBAQKU-ppHNSMHt0mrsoLwHSnd1ANYiVYHa58nut4QwRMts21zxHweP7kZXjwwq4mz45bEtp75Z8D-iEj6QuAxAznZa30yrQ3DGU5P0u7PRQK0C9APLHix1KC16\", \"title\": \"河海大学_百度百科\"}, {\"content\": \"2019年4月27日 - 6.《环境抗争中的怨恨心理研究》,《中国地质大学学报》(社会科学版),2015年第2期。 7.《国家介入背景下的底层环境抗争研究》,《河海大学学报》(哲学社...\", \"url\": \"http://www.baidu.com/link?url=vamqJTx6C4KiNFphQtL2oHaIA0QFVtfmFmVdOXsX2ZDozve3YbRbkxFBVm-jIOB48BXtikUaLacfQec0JfByYK\", \"title\": \"河海大学公共管理学院\"}, {\"content\": \"2006.9—2010.6 华中师范大学 社会学院社会学专业,法学学士。 工作情况: 2016年6月至今,河海大学公共管理学院社会学系工作。 (其中,2016年11月至2017年11月,借...\", \"url\": \"http://www.baidu.com/link?url=V1lHddB9dd-VmlOZmRAup1iMWmfsRPzFDpYeeCbm7PKE5hhB90vIu44uNRa5dRxAqcuwuDA-_L5J90hd9tsJn_\", \"title\": \"河海大学公共管理学院\"}, {\"content\": \"学院举办河海大学2019年植树活动暨港口海岸与近海工程... 2019年3月12日上午,在严恺馆广场前,学院携手河海大学后勤管理... 我院教师参加2019年高等学校实验...\", \"url\": \"http://www.baidu.com/link?url=K4GUH4FrnBvFf92nUQJ_AngiVsJZ56lvsHRM6lOq02n9cgREQWFARsv-qixw2taG\", \"title\": \"河海大学港口海岸与近海工程学院\"}, {\"content\": \"河海大学我要报名一流学科 211 手机网站 掌上研招人气指数:1775206 加入院校对比 院校代码:10294参考书目 报考指南 报名重要提醒 网上报名流程 招生考试公告 网上报名...\", \"url\": \"http://www.baidu.com/link?url=5XiCcWgvThzcttMnRnib7FvNGHHS8LSvFNGynfxbAmepOO9mAP0t6tvamXN6QFiytZhJpoLTMLg92TiIiiJ4NK\", \"title\": \"河海大学考研参考书目_河海大学研究生招生信息网-研究生招生报名...\"}, {\"content\": \"2019年5月15日 -    河海大学建立了以助学贷款为基本帮困渠道、勤工助学为主要手段、奖助学金为激励方式、减免学费和困难补助为辅助措施的多元化资助经济困难学生的...\", \"url\": \"http://www.baidu.com/link?url=E3M9-rabCU1b2nQke2D406g4z88uh4Vr9_D7uC-Qm00IcxENS6FFcaWQ40rWs-_-rM6hTCh_qYDIogGsYXP1z7vi_Vu5afheFOQ-OxhDZO6c98_u07S3rBRp7qdHWKjd\", \"title\": \"河海大学_院校信息库_阳光高考\"}, {\"content\": \"2019-04-16 河海大学常州校区留学生在江苏外国人定... 2019-04-15 我校与华东院签署共建留学生实习实践基... 2019-04-01通知公告 >> How...\", \"url\": \"http://www.baidu.com/link?url=yKsrjG5yZ-FK9gEUOXZni7i1GFdmRJfF2I-hdi4K1TiQm3tekhbKIb34AMlBKqzB\", \"title\": \"河海大学国际教育学院\"}, {\"content\": \"河海大学研究生招生目录及参考书 2014 商学院 专业名称:020201国民经济学 招生人数 请咨询招生单位 研究方向 (01)金融工程与投资管理,(02)国际金融,(03)国民经济...\", \"url\": \"http://www.baidu.com/link?url=repBkvy4XCk632AKCUvtakZ8atPh2_ymqhF2w_wPCqUnX4oNtTfgue5iAWh_Panl62vDod_TNRge-cVHOdO18c_pPwqoOdASpTF56YUVzBu\", \"title\": \"河海大学2019年研究生招生目录_河海大学2019年考研复习参考书_...\"}, {\"content\": \"2019年5月23日 - 河海大学,河海大学官方微博、教育官微联盟成员。河海大学的微博主页、个人资料、相册。新浪微博,随时随地分享身边的新鲜事儿。\", \"url\": \"http://www.baidu.com/link?url=lBnhiVZFe2XbWQNhY3hWziyjzIefOxj8nLrChZJ_tTpujaGBG8uTy-ABSkVtGa1o-FIKkUbqk8qDtwrB-OdMeK\", \"title\": \"河海大学的微博_微博\"}]}";
        try {
            JSONObject object = new JSONObject(a);
            String info = object.getString("answer");
            System.out.println(info);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}