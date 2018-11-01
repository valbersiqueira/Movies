
package valber.com.br.movies.domain;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultVideo implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieVideo> movieVideos = null;
    private final static long serialVersionUID = -7433522148958862330L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieVideo> getMovieVideos() {
        return movieVideos;
    }

    public void setMovieVideos(List<MovieVideo> movieVideos) {
        this.movieVideos = movieVideos;
    }

}
