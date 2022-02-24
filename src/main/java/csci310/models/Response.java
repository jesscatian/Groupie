package csci310.models;

public class Response<T> {
    private Boolean status;
    private String message;
    private T data;

    public Response() {}

    public Response(Boolean status) {
        this.status = status;
    }

    public Response(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(Boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
