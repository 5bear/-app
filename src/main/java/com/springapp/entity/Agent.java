package com.springapp.entity;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by 11369 on 2016/10/6.
 * 经销商
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Agent {
    //添加字段 经销商编号 预留编号可以为空
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String agent; //经销商名字
    private String agentNo;//经销商编号
    private Long uid; //用户id
    @Transient
    private String pinyin;//经销商拼音
    @Transient
    private String pinyinAbbr;//经销商拼音缩写
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPinyin() {
        try {
            pinyin = converterToSpellAll(agent);
            return pinyin.replaceAll(" ","");
        }catch (Exception e){
            return "fail";
        }
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinAbbr() {
        try {
            pinyinAbbr = converterToSpell(agent.trim());
            return pinyinAbbr.replaceAll(" ","");
        }catch (Exception e){
            return "fail";
        }
    }

    public void setPinyinAbbr(String pinyinAbbr) {
        this.pinyinAbbr = pinyinAbbr;
    }

    /*
        汉字转拼音缩写
         */
    public static String converterToSpell(String chines){
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if(!Character.isLetter(nameChar[i]))
                continue;
            if (nameChar[i] > 128) {
                try {
                    String pinyin = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                    pinyinName += pinyin.charAt(0) ;
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    /*全拼*/
    public static String converterToSpellAll(String chines){
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if(!Character.isLetter(nameChar[i]))
                continue;
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

}
