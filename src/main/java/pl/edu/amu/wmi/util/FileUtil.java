package pl.edu.amu.wmi.util;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.amu.wmi.model.BaseAttachment;

import javax.annotation.Nonnull;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    private static final CacheLoader<Integer, String> cacheLoader = new CacheLoader<Integer, String>() {
        @Override
        public String load(@Nonnull Integer key) {
            return UUID.randomUUID().toString();
        }
    };

    private FileUtil() {
    }

    private static String prepareFileName(String fileName, boolean ie) {
        String charsetName = ie ? "windows-1250" : "utf-8";
        try {
            byte[] fileNameBytes = fileName.getBytes(charsetName);
            StringBuilder dispositionFileName = new StringBuilder();
            for (byte b : fileNameBytes) {
                char newChar = (char) (b & 0xff);
                dispositionFileName.append(newChar);
            }
            return dispositionFileName.toString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("", e);
            return null;
        }
    }

    public static boolean isInternetExplorer(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return (userAgent.contains("MSIE"));
    }

    public static void prepareFileResponse(HttpServletRequest servletRequest,
                                           HttpServletResponse response,
                                           BaseAttachment baseAttachment) {
        try {
            setFileName(servletRequest, response, baseAttachment.getUrl());
            ServletOutputStream out = response.getOutputStream();
            out.write(baseAttachment.getPlikBin());
            out.flush();
            out.close();
        } catch (IOException e) {
            LOGGER.error("Error during attachment preparation.", e);
        }
    }

    public static void setFileName(HttpServletRequest servletRequest, HttpServletResponse response, String url) {
        boolean internetExplorer = FileUtil.isInternetExplorer(servletRequest);
        String preparedFileName = prepareFileName(url, internetExplorer);
        response.setHeader("Content-Disposition", "attachment; filename=" + preparedFileName);
    }

    public static String dateFileName(String prefix) {
        final String dateFileName = new SimpleDateFormat("yyyyMMddHHmm'.xls'").format(new Date());
        return prefix + dateFileName;
    }

    public static String stringFromFilename(URL fileName) {
        try {
            return Resources.toString(fileName, Charsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Cannot find file", e);
        }
        return null;
    }

    public static LoadingCache<Integer, String> loadingCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .build(cacheLoader);
    }

}
