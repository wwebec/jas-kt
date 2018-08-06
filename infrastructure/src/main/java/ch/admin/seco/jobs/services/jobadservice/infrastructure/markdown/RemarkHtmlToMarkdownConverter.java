package ch.admin.seco.jobs.services.jobadservice.infrastructure.markdown;

import java.util.regex.Pattern;

import com.overzealous.remark.Remark;

import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.HtmlToMarkdownConverter;

@Service
public class RemarkHtmlToMarkdownConverter implements HtmlToMarkdownConverter {

    final private Pattern htmlTagPattern = Pattern.compile("<(?:\"[^\"]*\"['\"]*|'[^']*'['\"]*|[^'\">])+>");
    final private Remark remark = new Remark();

    @Override
    public String convert(String source) {
        if (source == null) {
            return null;
        }

        if (!containsHtmlMarkup(source)) {
            return source;
        }

        return remark.convert(preserveLineBreaks(source));
    }

    private boolean containsHtmlMarkup(String source) {
        return htmlTagPattern.matcher(source).find();
    }

    private String preserveLineBreaks(String source) {
        return source.replaceAll("(\\\\r\\\\n|\\\\n)", "<br/>");
    }
}
