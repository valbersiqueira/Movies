
package valber.com.br.movies.domain;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lancamentos implements Serializable
{

    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("dates")
    @Expose
    private DatesLancamento dates;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    private final static long serialVersionUID = 9177197279892329791L;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public DatesLancamento getDates() {
        return dates;
    }

    public void setDates(DatesLancamento datesLancamento) {
        this.dates = datesLancamento;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}
