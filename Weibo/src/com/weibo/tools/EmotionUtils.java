package com.weibo.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.example.weibo.R;



public class EmotionUtils implements Serializable{
	
	public static Map<String, Integer> emojiMap;

	static{
		emojiMap = new HashMap<String, Integer>();
		emojiMap.put("[爱你]", R.drawable.d_aini);
		emojiMap.put("[奥特曼]", R.drawable.d_aoteman);
		emojiMap.put("[拜拜]", R.drawable.d_baibai);
		emojiMap.put("[悲伤]", R.drawable.d_beishang);
		emojiMap.put("[鄙视]", R.drawable.d_bishi);
		emojiMap.put("[闭嘴]", R.drawable.d_bizui);
		emojiMap.put("[不要]", R.drawable.h_buyao);
		emojiMap.put("[馋嘴]", R.drawable.d_chanzui);
		emojiMap.put("[吃惊]", R.drawable.d_chijing);
		emojiMap.put("[good]", R.drawable.h_good);
		emojiMap.put("[haha]", R.drawable.h_haha);
		emojiMap.put("[汗]", R.drawable.d_han);
		emojiMap.put("[笑cry]", R.drawable.d_xiaoku);
	    
		emojiMap.put("[晕]", R.drawable.d_yun);
		emojiMap.put("[抓狂]", R.drawable.d_zhuakuang);
		emojiMap.put("[猪头]", R.drawable.d_zhutou);
		emojiMap.put("[最右]", R.drawable.d_zuiyou);
		emojiMap.put("[左哼哼]", R.drawable.d_zuohengheng);

		emojiMap.put("[来]", R.drawable.h_lai);
		emojiMap.put("[ok]", R.drawable.h_ok);
		emojiMap.put("[弱]", R.drawable.h_ruo);
		emojiMap.put("[握手]", R.drawable.h_woshou);
		emojiMap.put("[耶]", R.drawable.h_ye);
		emojiMap.put("[赞]", R.drawable.h_zan);
	    emojiMap.put("[泪]", R.drawable.d_lei);
	    emojiMap.put("[黑线]", R.drawable.d_heixian);
	    emojiMap.put("[作揖]", R.drawable.h_zuoyi);
	    emojiMap.put("[亲亲]", R.drawable.d_qinqin);
	    emojiMap.put("[傻眼]", R.drawable.d_shayan);
	    emojiMap.put("[生病]", R.drawable.d_shengbing);
	    emojiMap.put("[草泥马]", R.drawable.d_shenshou);
	    emojiMap.put("[熊猫]", R.drawable.d_xiongmao);
	    emojiMap.put("[嘻嘻]", R.drawable.d_xixi);
	    emojiMap.put("[嘘]", R.drawable.d_xu);
	    emojiMap.put("[浪]", R.drawable.d_lang);
	    emojiMap.put("[蛋糕]", R.drawable.o_dangao);
	    emojiMap.put("[飞机]", R.drawable.o_feiji);
	    emojiMap.put("[干杯]", R.drawable.o_ganbei);
	    emojiMap.put("[话筒]", R.drawable.o_huatong);
	    emojiMap.put("[躁狂症]", R.drawable.kuangzaozheng);
	    emojiMap.put("[蜡烛]", R.drawable.o_lazhu);
	    emojiMap.put("[礼物]", R.drawable.o_liwu);
	    emojiMap.put("[绿丝带]", R.drawable.o_lvsidai);
	    emojiMap.put("[围脖]", R.drawable.o_weibo);
	    emojiMap.put("[喵喵]", R.drawable.d_miao);
	    emojiMap.put("[浮云]", R.drawable.w_fuyun);
	    emojiMap.put("[沙尘暴]", R.drawable.w_shachenbao);
	    emojiMap.put("[太阳]", R.drawable.w_taiyang);
	    emojiMap.put("[微风]", R.drawable.w_weifeng);
	    emojiMap.put("[鲜花]", R.drawable.w_xianhua);
	    emojiMap.put("[下雨]", R.drawable.w_xiayu);
	    emojiMap.put("[月亮]", R.drawable.w_yueliang);
	    emojiMap.put("[围观]", R.drawable.o_weiguan);
	    emojiMap.put("[音乐]", R.drawable.o_yinyue);
	    emojiMap.put("[照相机]", R.drawable.o_zhaoxiangji);
	    emojiMap.put("[钟]", R.drawable.o_zhong);
	    emojiMap.put("[哼]", R.drawable.d_heng);
	    emojiMap.put("[色]", R.drawable.d_huaxin);
	    emojiMap.put("[白眼]", R.drawable.d_landelini);
	    emojiMap.put("[怒]", R.drawable.d_nu);
	    emojiMap.put("[伤心]", R.drawable.l_shangxin);
	    emojiMap.put("[心]", R.drawable.l_xin);
	    emojiMap.put("[NO]", R.drawable.h_buyao);
	    emojiMap.put("[女孩儿]", R.drawable.d_nvhaier);
	    emojiMap.put("[男孩儿]", R.drawable.d_nanhaier);
	    emojiMap.put("[钱]", R.drawable.d_qian);
	    emojiMap.put("[微笑]", R.drawable.d_hehe);
	    emojiMap.put("[挤眼]", R.drawable.d_jiyan);
	    emojiMap.put("[威武]", R.drawable.f_v5);
	    emojiMap.put("[喜]", R.drawable.f_xi);
	    emojiMap.put("[可怜]", R.drawable.d_kelian);
	    emojiMap.put("[酷]", R.drawable.d_ku);
	    emojiMap.put("[太开心]", R.drawable.d_taikaixin);
	    emojiMap.put("[偷笑]", R.drawable.d_touxiao);
	    emojiMap.put("[阴险]", R.drawable.d_yinxian);
	    emojiMap.put("[疑问]", R.drawable.d_yiwen);
	    emojiMap.put("[右哼哼]", R.drawable.d_youhengheng);
	    emojiMap.put("[可爱]", R.drawable.d_keai);
	    emojiMap.put("[失望]", R.drawable.d_shiwang);
	    emojiMap.put("[衰]", R.drawable.d_shuai);
	    emojiMap.put("[吐]", R.drawable.d_tu);
	    emojiMap.put("[兔子]", R.drawable.d_tuzi);
	    emojiMap.put("[怒骂]", R.drawable.d_numa);
	    emojiMap.put("[挖鼻]", R.drawable.d_wabishi);
	    emojiMap.put("[委屈]", R.drawable.d_weiqu);
	    emojiMap.put("[睡]", R.drawable.d_shuijiao);
	    emojiMap.put("[思考]", R.drawable.d_sikao);
	    emojiMap.put("[织]", R.drawable.f_zhi);
	    emojiMap.put("[困]", R.drawable.d_kun);
	    emojiMap.put("[给力]", R.drawable.f_geili);
	    emojiMap.put("[互粉]", R.drawable.f_hufen);
	    emojiMap.put("[囧]", R.drawable.f_jiong);
	    emojiMap.put("[萌]", R.drawable.f_meng);
	    emojiMap.put("[神马]", R.drawable.f_shenma);
	    emojiMap.put("[哈欠]", R.drawable.d_dahaqi);
	    emojiMap.put("[打脸]", R.drawable.d_dalian);
	    emojiMap.put("[顶]", R.drawable.d_ding);
	    emojiMap.put("[doge]", R.drawable.d_doge);
	    emojiMap.put("[肥皂]", R.drawable.d_feizao);
	    emojiMap.put("[感冒]", R.drawable.d_ganmao);
	    emojiMap.put("[鼓掌]", R.drawable.d_guzhang);
	    emojiMap.put("[哈哈]", R.drawable.d_haha);
	    emojiMap.put("[害羞]", R.drawable.d_haixiu);
	    emojiMap.put("[悲催]", R.drawable.lxh_beicui);
	    emojiMap.put("[被电]", R.drawable.lxh_beidian);
	    emojiMap.put("[崩溃]", R.drawable.lxh_bengkui);
	    emojiMap.put("[别烦我]", R.drawable.lxh_biefanwo);
	    emojiMap.put("[不好意思]", R.drawable.lxh_buhaoyisi);
	    emojiMap.put("[不想上班]", R.drawable.lxh_buxiangshangban);
	    emojiMap.put("[得意地笑]", R.drawable.lxh_deyidexiao);
	    emojiMap.put("[给劲]", R.drawable.lxh_feijin);
	    emojiMap.put("[好爱哦]", R.drawable.lxh_haoaio);
	    emojiMap.put("[好棒]", R.drawable.lxh_haobang);
	    emojiMap.put("[好囧]", R.drawable.lxh_haojiong);
	    emojiMap.put("[好喜欢]", R.drawable.lxh_haoxihuan);
	    emojiMap.put("[hold住]", R.drawable.lxh_holdzhu);
	    emojiMap.put("[杰克逊]", R.drawable.lxh_jiekexun);
	    emojiMap.put("[纠结]", R.drawable.lxh_jiujie);
	    emojiMap.put("[巨汗]", R.drawable.lxh_juhan);
	    emojiMap.put("[抠鼻屎]", R.drawable.lxh_koubishi);
	    emojiMap.put("[困死了]", R.drawable.lxh_kunsile);
	    emojiMap.put("[雷锋]", R.drawable.lxh_leifeng);
	    emojiMap.put("[泪流满面]", R.drawable.lxh_leiliumanmian);
	    emojiMap.put("[玫瑰]", R.drawable.lxh_meigui);
	    emojiMap.put("[噢耶]", R.drawable.lxh_oye);
	    emojiMap.put("[霹雳]", R.drawable.lxh_pili);
	    emojiMap.put("[瞧瞧]", R.drawable.lxh_qiaoqiao);
	    emojiMap.put("[丘比特]", R.drawable.lxh_qiubite);
	    emojiMap.put("[求关注]", R.drawable.lxh_qiuguanzhu);
	    emojiMap.put("[群体围观]", R.drawable.lxh_quntiweiguan);
	    emojiMap.put("[甩甩手]", R.drawable.lxh_shuaishuaishou);
	    emojiMap.put("[偷乐]", R.drawable.lxh_toule);
	    emojiMap.put("[推荐]", R.drawable.lxh_tuijian);
	    emojiMap.put("[互相膜拜]", R.drawable.lxh_xianghumobai);
	    emojiMap.put("[想一想]", R.drawable.lxh_xiangyixiang);
	    emojiMap.put("[笑哈哈]", R.drawable.lxh_xiaohaha);
	    emojiMap.put("[羞嗒嗒]", R.drawable.lxh_xiudada);
	    emojiMap.put("[许愿]", R.drawable.lxh_xuyuan);
	    emojiMap.put("[有鸭梨]", R.drawable.lxh_youyali);
	    emojiMap.put("[赞啊]", R.drawable.lxh_zana);
	    emojiMap.put("[震惊]", R.drawable.lxh_zhenjing);
	    emojiMap.put("[转发]", R.drawable.lxh_zhuanfa);
	    emojiMap.put("[链接]", R.drawable.timeline_card_small_web);
	    emojiMap.put("[认证]", R.drawable.emotion_vip);
	}
	
	public static int getImgByName(String imgName) {
		Integer integer = emojiMap.get(imgName);
		return integer == null ? -1 : integer;
	}
}
