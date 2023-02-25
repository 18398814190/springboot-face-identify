package com.wjp.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class CommonUtils {


    /**
     * 文件后缀 支持的类型
     */
    private static final String[] FILE_SUFFIX_SUPPORT = {".jpg", ".jpeg", ".png"};

    /**
     * 文件名字 需要排除的字符
     */
    private static final String[] FILE_NAME_EXCLUDE = {
            "`", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "=", "_", "+",
            "~", "·", "！", "￥", "……", "（", "）", "——",
            "?", ",", "<", ">", ":", ";", "[", "]", "{", "}", "/", "\\", "|",
            "？", "，", "。", "《", "》", "：", "；", "【", "】", "、",
    };

    /**
     * 文件大小 10MB todo 自行更改
     */
    private static final long FILE_SIZE = 100 * 1024 * 1024;

    /**
     * 上传文件校验大小、名字、后缀
     * @param multipartFile multipartFile
     */
    public static void uploadVerify(MultipartFile multipartFile) {
        // 校验文件是否为空
        if (multipartFile == null) {
            throw new RuntimeException("文件不能为空！");
        }

        // 校验文件大小
        long size = multipartFile.getSize();
        if(size > FILE_SIZE){
            throw new RuntimeException("文件大小不能超过10MB！");
        }

        // 校验文件名字
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("文件名字不能为空！");
        }
        boolean nameFlag = false;
        for (String str : FILE_NAME_EXCLUDE) {
            if (originalFilename.contains(str)) {
                nameFlag = true;
                break;
            }
        }
        if(nameFlag){
            throw new RuntimeException("文件名字不允许出现"+ Arrays.toString(FILE_NAME_EXCLUDE) +"关键字！");
        }

        // 校验文件后缀
        if (!originalFilename.contains(".")) {
            throw new RuntimeException("文件不能没有后缀！");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        boolean flag = true;
        for (String s : FILE_SUFFIX_SUPPORT) {
            if (s.equals(suffix.toLowerCase(Locale.ROOT))) {
                flag = false;
                break;
            }
        }
        if(flag){
            throw new RuntimeException("文件格式仅限于"+ Arrays.toString(FILE_SUFFIX_SUPPORT) +"！");
        }
    }


    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;
        if(length == 4){
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if(code < 1000){
                code = code + 1000;//保证随机数为4位数字
            }
        }else if(length == 6){
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if(code < 100000){
                code = code + 100000;//保证随机数为6位数字
            }
        }else{
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }



}
