package fuzzyverse;

import java.util.ArrayList;
import java.util.List;

public class UrlFilter {

    public Boolean ReturnCodeIs(UrlInfo url, String returnCode)
    {
        if(url.ReturnCode == Integer.parseInt(returnCode))
            return false;
        return true;
    }

    public Boolean ReturnCodeIsNot(UrlInfo url, String returnCode)
    {
        if(url.ReturnCode != Integer.parseInt(returnCode))
            return false;
        return true;
    }

    public Boolean SizeIs(UrlInfo url, String size)
    {
        if(url.Size == Integer.parseInt(size))
            return false;
        return true;
    }

    public Boolean SizeIsNot(UrlInfo url, String size)
    {
        if(url.Size != Integer.parseInt(size))
            return false;
        return true;
    }

    public Boolean SizeIsLessThan(UrlInfo url, String size)
    {
        if(url.Size < Integer.parseInt(size))
            return false;
        return true;
    }

    public Boolean SizeIsGreaterThan(UrlInfo url, String size)
    {
        if(url.Size > Integer.parseInt(size))
            return false;
        return true;
    }

    public Boolean DomainContains(UrlInfo url, String str)
    {
        if(url.Url == str)
            return false;
        return true;
    }

    public List<UrlInfo> Execute(List<UrlInfo> urls, List<FilterItem> filters)
    {
        List<UrlInfo> output = new ArrayList<UrlInfo>();

        for(UrlInfo url : urls)
        {
            Boolean isFiltered = false;

            for(FilterItem filter : filters)
            {
                if(filter.Type == FilterType.DomainContains)
                    isFiltered = DomainContains(url, filter.Value);
                if(filter.Type == FilterType.ReturnCodeIs)
                    isFiltered = ReturnCodeIs(url, filter.Value);
                if(filter.Type == FilterType.ReturnCodeIsNot)
                    isFiltered = ReturnCodeIsNot(url, filter.Value);
                if(filter.Type == FilterType.SizeIs)
                    isFiltered = SizeIs(url, filter.Value);
                if(filter.Type == FilterType.SizeIsNot)
                    isFiltered = SizeIsNot(url, filter.Value);
                if(filter.Type == FilterType.SizeIsGreaterThan)
                    isFiltered = SizeIsGreaterThan(url, filter.Value);
                if(filter.Type == FilterType.SizeIsLessThan)
                    isFiltered = SizeIsLessThan(url, filter.Value);
            }

            if(!isFiltered)
                output.add(url);
        }

        return output;
    }

    public String GetFilterTypeString(FilterType type)
    {
        if(type == FilterType.ReturnCodeIs)
            return "Return Code Is";
        if(type == FilterType.ReturnCodeIsNot)
            return "Return Code Is Not";
        if(type == FilterType.SizeIs)
            return "Size Is";
        if(type == FilterType.SizeIsGreaterThan)
            return "Size Is Greater Than";
        if(type == FilterType.SizeIsLessThan)
            return "Size Is Less Than";
        if(type == FilterType.SizeIsNot)
            return "Size Is Not";
        if(type == FilterType.DomainContains)
            return "Domain Contains";

        return "";
    }

    public FilterType GetFilterTypeFromString(String type)
    {
        if(type == "Return Code Is")
            return FilterType.ReturnCodeIs;
        if(type == "Return Code Is Not")
            return FilterType.ReturnCodeIsNot;
        if(type == "Size Is")
            return FilterType.SizeIs;
        if(type == "Size Is Greater Than")
            return FilterType.SizeIsGreaterThan;
        if(type == "Size Is Less Than")
            return FilterType.SizeIsLessThan;
        if(type == "Size Is Not")
            return FilterType.SizeIsNot;
        if(type == "Domain Contains")
            return FilterType.DomainContains;

        return FilterType.ReturnCodeIs;
    }
}
