export const API_BASE =
    process.env.NODE_ENV === "development"
        ? "http://localhost:8080"
        : "http://api.budgment.nintech.engineer/";
